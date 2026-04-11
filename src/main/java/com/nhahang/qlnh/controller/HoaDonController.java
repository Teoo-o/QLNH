package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.*;
import com.nhahang.qlnh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin(origins = "*")
public class HoaDonController {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private BanAnRepository banAnRepository;
    @Autowired private MonAnRepository monAnRepository;
    @Autowired private HdMaRepository hdMaRepository;
    @Autowired private NguyenLieuRepository nguyenLieuRepository;
    @Autowired private MaNlRepository maNlRepository;

    // 1. TẠO HÓA ĐƠN HOẶC GỌI THÊM MÓN
    @PostMapping("/tao-moi")
    @Transactional
    public ResponseEntity<?> taoHoaDon(@RequestBody Map<String, Object> payload) {
        try {
            String maBan = (String) payload.get("maBan");
            List<Map<String, Object>> chiTietList = (List<Map<String, Object>>) payload.get("chiTietList");

            BanAn ban = banAnRepository.findById(maBan).orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

            List<HoaDon> hdChuaThanhToan = hoaDonRepository.findByBanAn_MaBanAndPttt(maBan, "Chưa thanh toán");
            HoaDon hoaDon;

            if (hdChuaThanhToan.isEmpty()) {
                hoaDon = new HoaDon();
                hoaDon.setMaHD("HD" + System.currentTimeMillis());
                hoaDon.setBanAn(ban);
                hoaDon.setNgayTao(LocalDateTime.now());
                hoaDon.setLoaiHD("Tại chỗ");
                hoaDon.setPttt("Chưa thanh toán");
                hoaDon = hoaDonRepository.save(hoaDon);
            } else {
                hoaDon = hdChuaThanhToan.get(0);
            }

            for (Map<String, Object> item : chiTietList) {
                String maMon = (String) item.get("maMon");
                Integer soLuongMoi = Integer.parseInt(item.get("soLuong").toString());

                Object donGiaObj = item.get("donGia") != null ? item.get("donGia") : item.get("dongGia");
                Double donGia = Double.parseDouble(donGiaObj.toString());

                MonAn mon = monAnRepository.findById(maMon).orElse(null);
                if (mon != null) {
                    HdMaKey key = new HdMaKey(hoaDon.getMaHD(), maMon);
                    Optional<HdMa> chiTietCu = hdMaRepository.findById(key);

                    if (chiTietCu.isPresent()) {
                        HdMa ct = chiTietCu.get();
                        ct.setSl(ct.getSl() + soLuongMoi);
                        hdMaRepository.save(ct);
                    } else {
                        HdMa chiTietMoi = new HdMa();
                        chiTietMoi.setId(key);
                        chiTietMoi.setHoaDon(hoaDon);
                        chiTietMoi.setMonAn(mon);
                        chiTietMoi.setSl(soLuongMoi);
                        chiTietMoi.setDonGiaBan(donGia);
                        hdMaRepository.save(chiTietMoi);
                    }
                }
            }

            ban.setStatus("Có khách");
            banAnRepository.save(ban);

            return ResponseEntity.ok(Collections.singletonMap("maHD", hoaDon.getMaHD()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi xử lý Order: " + e.getMessage()));
        }
    }

    // 2. MỞ BÀN ĐỎ LẤY HÓA ĐƠN RA TÍNH TIỀN
    @GetMapping("/ban/{maBan}/chua-thanh-toan")
    public ResponseEntity<?> getHoaDonChuaThanhToan(@PathVariable String maBan) {
        List<HoaDon> listHD = hoaDonRepository.findByBanAn_MaBanAndPttt(maBan, "Chưa thanh toán");
        if (listHD.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Bàn này chưa có hóa đơn hoặc đã thanh toán!"));
        }

        HoaDon hd = listHD.get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("maHD", hd.getMaHD());
        response.put("ngayTao", hd.getNgayTao());

        List<HdMa> chiTietList = hdMaRepository.findByHoaDon_MaHD(hd.getMaHD());
        List<Map<String, Object>> dsMon = new ArrayList<>();

        for (HdMa ct : chiTietList) {
            Map<String, Object> monData = new HashMap<>();
            monData.put("sl", ct.getSl());
            monData.put("soLuong", ct.getSl());
            monData.put("donGiaBan", ct.getDonGiaBan());
            monData.put("donGia", ct.getDonGiaBan());

            Map<String, String> thongTinMon = new HashMap<>();
            thongTinMon.put("maMon", ct.getMonAn().getMaMon());
            thongTinMon.put("tenMon", ct.getMonAn().getTenMon());
            thongTinMon.put("hinhAnh", ct.getMonAn().getHinhAnh());

            monData.put("monAn", thongTinMon);
            dsMon.add(monData);
        }
        response.put("chiTietList", dsMon);

        return ResponseEntity.ok(response);
    }

    // 3. XÁC NHẬN THU TIỀN (ĐÃ TÍCH HỢP LƯU KHUYẾN MÃI)
    @PutMapping("/thanh-toan")
    @Transactional
    public ResponseEntity<?> thanhToan(@RequestBody Map<String, Object> payload) {
        try {
            String maHD = (String) payload.get("maHD");
            String maBan = (String) payload.get("maBan");
            String pttt = (String) payload.get("pttt");

            // Lấy thông tin Khuyến mãi gửi từ Web xuống
            String maKM = (String) payload.get("maKM");
            Double soTienGiam = payload.get("soTienGiam") != null ? Double.parseDouble(payload.get("soTienGiam").toString()) : 0.0;

            HoaDon hd = hoaDonRepository.findById(maHD)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

            hd.setPttt(pttt);
            hd.setMaKM(maKM);
            hd.setSoTienGiam(soTienGiam); // Lưu số tiền được giảm vào Database
            hoaDonRepository.save(hd);

            // Xóa khách khỏi bàn
            if (maBan != null) {
                BanAn ban = banAnRepository.findById(maBan).orElse(null);
                if (ban != null) {
                    ban.setStatus("Trống");
                    banAnRepository.save(ban);
                }
            }

            // TRỪ KHO TỰ ĐỘNG
            List<HdMa> chiTietList = hdMaRepository.findByHoaDon_MaHD(maHD);
            for (HdMa chiTiet : chiTietList) {
                String maMon = chiTiet.getMonAn().getMaMon();
                int soLuongMonBanRa = chiTiet.getSl();

                List<MaNl> congThuc = maNlRepository.findByMonAn_MaMon(maMon);
                for (MaNl ct : congThuc) {
                    String maNL = ct.getNguyenLieu().getMaNL();
                    Double dinhLuong = ct.getDinhLuong();

                    NguyenLieu kho = nguyenLieuRepository.findById(maNL).orElse(null);
                    if (kho != null) {
                        double tongTieuHao = dinhLuong * soLuongMonBanRa;
                        kho.setSl(kho.getSl() - tongTieuHao);
                        nguyenLieuRepository.save(kho);
                    }
                }
            }

            return ResponseEntity.ok(Collections.singletonMap("message", "Thanh toán thành công và đã trừ kho!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
}