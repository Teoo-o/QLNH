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
            String thoiGian = payload.get("thoiGian");
            int soNguoi = Integer.parseInt(payload.get("soNguoi"));
            String maBan = payload.get("maBan");

            KhachHang kh = new KhachHang();
            kh.setMaKH("KH" + System.currentTimeMillis());
            kh.setTenKH(tenKhach);
            kh.setSdt(sdt);
            khachHangRepository.save(kh);

            PhieuDatBan pd = new PhieuDatBan();
            pd.setMaPhieu("PD" + System.currentTimeMillis());
            pd.setKhachHang(kh);
            pd.setThoiGian(LocalDateTime.parse(thoiGian));
            pd.setSoNguoi(soNguoi);
            pd.setTrangThai("Chờ nhận bàn");

            if (maBan != null && !maBan.isEmpty()) {
                BanAn ban = banAnRepository.findById(maBan).orElse(null);
                if (ban != null) {
                    if (soNguoi > ban.getSoGhe()) {
                        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Số người (" + soNguoi + ") vượt quá sức chứa!"));
                    }
                    pd.setBanAn(ban);

                    // CHỈ KHÓA BÀN NẾU THỜI GIAN ĐẶT DƯỚI 2 TIẾNG TÍNH TỪ BÂY GIỜ
                    if (pd.getThoiGian().isBefore(LocalDateTime.now().plusHours(2))) {
                        if ("Trống".equals(ban.getStatus()) || ban.getStatus() == null) {
                            ban.setStatus("Đã đặt");
                            banAnRepository.save(ban);
                        }
                    }
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

            if (now.isBefore(phieu.getThoiGian())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Chưa đến giờ hẹn! Không thể nhận bàn."));
            }
            if (now.isAfter(phieu.getThoiGian().plusMinutes(15))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Phiếu đã lố 15 phút và bị vô hiệu hóa!"));
            }

            phieu.setTrangThai("Đã nhận bàn");
            phieuDatBanRepository.save(phieu);

            String maBan = "";
            if (phieu.getBanAn() != null) {
                BanAn ban = phieu.getBanAn();
                ban.setStatus("Có khách");
                banAnRepository.save(ban);
                maBan = ban.getMaBan();
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Nhận bàn thành công!");
            response.put("maBan", maBan);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy phiếu đặt bàn này!")));
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
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy phiếu đặt bàn này!")));
    }
}