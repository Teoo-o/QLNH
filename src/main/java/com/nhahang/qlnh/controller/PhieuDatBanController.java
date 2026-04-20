package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.BanAn;
import com.nhahang.qlnh.entity.KhachHang;
import com.nhahang.qlnh.entity.PhieuDatBan;
import com.nhahang.qlnh.repository.BanAnRepository;
import com.nhahang.qlnh.repository.KhachHangRepository;
import com.nhahang.qlnh.repository.PhieuDatBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/datban")
@CrossOrigin(origins = "*")
public class PhieuDatBanController {

    @Autowired private PhieuDatBanRepository phieuDatBanRepository;
    @Autowired private BanAnRepository banAnRepository;
    @Autowired private KhachHangRepository khachHangRepository;

    @GetMapping
    @Transactional
    public List<PhieuDatBan> layDanhSachDatBan() {
        List<PhieuDatBan> danhSach = phieuDatBanRepository.findAll();
        LocalDateTime thoiGianHienTai = LocalDateTime.now();
        boolean coThayDoi = false;

        for (PhieuDatBan phieu : danhSach) {
            if ("Chờ nhận bàn".equals(phieu.getTrangThai())) {
                if (thoiGianHienTai.isAfter(phieu.getThoiGian().plusMinutes(15))) {
                    phieu.setTrangThai("Đã hủy");
                    if (phieu.getBanAn() != null && "Đã đặt".equals(phieu.getBanAn().getStatus())) {
                        BanAn ban = phieu.getBanAn();
                        ban.setStatus("Trống");
                        banAnRepository.save(ban);
                    }
                    coThayDoi = true;
                }
            }
        }
        if (coThayDoi) phieuDatBanRepository.saveAll(danhSach);
        return danhSach;
    }

    @PostMapping("/tao-moi")
    @Transactional
    public ResponseEntity<?> taoPhieuDat(@RequestBody Map<String, String> payload) {
        try {
            String tenKhach = payload.get("tenKhach");
            String sdt = payload.get("sdt");
            LocalDateTime thoiGianDat = LocalDateTime.parse(payload.get("thoiGian"));
            int soNguoi = Integer.parseInt(payload.get("soNguoi"));
            String maBan = payload.get("maBan");

            if (maBan == null || maBan.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Vui lòng chọn bàn!"));
            }

            // 1. KIỂM TRA XUNG ĐỘT THỜI GIAN (Chặn +/- 2 tiếng)
            LocalDateTime startCheck = thoiGianDat.minusHours(2);
            LocalDateTime endCheck = thoiGianDat.plusHours(2);
            List<PhieuDatBan> trungLich = phieuDatBanRepository.findConflictingBookings(maBan, startCheck, endCheck);

            if (!trungLich.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message",
                        "Bàn này đã có người đặt trong khoảng thời gian từ " +
                                startCheck.toLocalTime() + " đến " + endCheck.toLocalTime() + ". Vui lòng chọn giờ khác hoặc bàn khác!"));
            }

            // 2. THUẬT TOÁN FIND-OR-CREATE KHACH HANG
            KhachHang kh = khachHangRepository.findBySdt(sdt).orElseGet(() -> {
                KhachHang khachMoi = new KhachHang();
                khachMoi.setMaKH("KH" + System.currentTimeMillis());
                khachMoi.setTenKH(tenKhach);
                khachMoi.setSdt(sdt);
                return khachHangRepository.save(khachMoi);
            });

            // 3. TẠO PHIẾU ĐẶT BÀN
            PhieuDatBan pd = new PhieuDatBan();
            pd.setMaPhieu("PD" + System.currentTimeMillis());
            pd.setKhachHang(kh);
            pd.setThoiGian(thoiGianDat);
            pd.setSoNguoi(soNguoi);
            pd.setTrangThai("Chờ nhận bàn");

            BanAn ban = banAnRepository.findById(maBan).orElse(null);
            if (ban != null) {
                if (soNguoi > ban.getSoGhe()) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Bàn này chỉ chứa được " + ban.getSoGhe() + " người!"));
                }
                pd.setBanAn(ban);

                // Nếu đặt bàn trong vòng 2 tiếng tới, cập nhật trạng thái bàn sang 'Đã đặt' ngay
                if (thoiGianDat.isBefore(LocalDateTime.now().plusHours(2))) {
                    ban.setStatus("Đã đặt");
                    banAnRepository.save(ban);
                }
            }

            phieuDatBanRepository.save(pd);
            return ResponseEntity.ok(Collections.singletonMap("message", "Tạo phiếu đặt bàn thành công!"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi: " + e.getMessage()));
        }
    }

    @PutMapping("/nhan-ban/{maPhieu}")
    @Transactional
    public ResponseEntity<?> khachNhanBan(@PathVariable String maPhieu) {
        return phieuDatBanRepository.findById(maPhieu).map(phieu -> {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(phieu.getThoiGian().minusMinutes(30))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Còn quá sớm! Quý khách vui lòng đợi thêm."));
            }
            if (now.isAfter(phieu.getThoiGian().plusMinutes(15))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Quá 15 phút hẹn! Phiếu đã bị hủy tự động."));
            }

            phieu.setTrangThai("Đã nhận bàn");
            phieuDatBanRepository.save(phieu);

            if (phieu.getBanAn() != null) {
                BanAn ban = phieu.getBanAn();
                ban.setStatus("Có khách");
                banAnRepository.save(ban);
            }

            return ResponseEntity.ok(Collections.singletonMap("message", "Nhận bàn thành công!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy phiếu!")));
    }

    @PutMapping("/huy/{maPhieu}")
    @Transactional
    public ResponseEntity<?> khachHuyBan(@PathVariable String maPhieu) {
        return phieuDatBanRepository.findById(maPhieu).map(phieu -> {
            phieu.setTrangThai("Đã hủy");
            phieuDatBanRepository.save(phieu);

            if (phieu.getBanAn() != null && "Đã đặt".equals(phieu.getBanAn().getStatus())) {
                BanAn ban = phieu.getBanAn();
                ban.setStatus("Trống");
                banAnRepository.save(ban);
            }
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã hủy phiếu đặt bàn!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi!")));
    }
}