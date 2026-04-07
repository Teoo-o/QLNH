package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.*;
import com.nhahang.qlnh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin(origins = "*")
public class HoaDonController {

    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private BanAnRepository banAnRepository;
    @Autowired
    private MonAnRepository monAnRepository;
    @Autowired
    private HdMaRepository hdMaRepository; // Đã đổi tên
    @Autowired
    private NguyenLieuRepository nguyenLieuRepository;
    @Autowired
    private MaNlRepository maNlRepository; // Đã đổi tên

    // 1. TẠO HÓA ĐƠN MỚI TỪ BỘ PHẬN ORDER
    @PostMapping("/tao-moi")
    @Transactional
    public ResponseEntity<?> taoHoaDon(@RequestBody Map<String, Object> payload) {
        try {
            String maBan = (String) payload.get("maBan");
            List<Map<String, Object>> chiTietList = (List<Map<String, Object>>) payload.get("chiTietList");

            String maHD = "HD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            HoaDon hd = new HoaDon();
            hd.setMaHD(maHD);
            hd.setLoaiHD("Tại chỗ");
            hd.setPttt("Chưa thanh toán");
            hd.setNgayTao(LocalDateTime.now());

            if (maBan != null) {
                BanAn ban = banAnRepository.findById(maBan).orElse(null);
                if (ban != null) {
                    ban.setStatus("Có khách");
                    banAnRepository.save(ban);
                    hd.setBanAn(ban);
                }
            }

            hoaDonRepository.save(hd);

            for (Map<String, Object> item : chiTietList) {
                String maMon = (String) item.get("maMon");
                Integer soLuong = Integer.parseInt(item.get("soLuong").toString());
                Double donGia = Double.parseDouble(item.get("dongGia").toString());

                MonAn mon = monAnRepository.findById(maMon).orElse(null);
                if (mon != null) {
                    HdMa chiTiet = new HdMa(); // Đã đổi class

                    // LƯU Ý: Nếu class khóa chính của bạn tên khác, hãy đổi tên 'HdMaKey' cho khớp nhé
                    HdMaKey key = new HdMaKey(maHD, maMon);

                    chiTiet.setId(key);
                    chiTiet.setHoaDon(hd);
                    chiTiet.setMonAn(mon);
                    chiTiet.setSl(soLuong);
                    chiTiet.setDonGiaBan(donGia);

                    hdMaRepository.save(chiTiet);
                }
            }
            return ResponseEntity.ok(Collections.singletonMap("maHD", maHD));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo hóa đơn: " + e.getMessage());
        }
    }

    // 2. LẤY HÓA ĐƠN CHƯA THANH TOÁN CHO POS
    @GetMapping("/ban/{maBan}/chua-thanh-toan")
    public ResponseEntity<?> getHoaDonChuaThanhToan(@PathVariable String maBan) {
        List<HoaDon> listHD = hoaDonRepository.findByBanAn_MaBanAndPttt(maBan, "Chưa thanh toán");
        if (listHD.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy hóa đơn chưa thanh toán cho bàn này!");
        }

        HoaDon hd = listHD.get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("maHD", hd.getMaHD());
        response.put("ngayTao", hd.getNgayTao());

        List<HdMa> chiTietList = hdMaRepository.findByHoaDon_MaHD(hd.getMaHD()); // Đã đổi tên
        List<Map<String, Object>> dsMon = new ArrayList<>();

        for (HdMa ct : chiTietList) {
            Map<String, Object> monData = new HashMap<>();
            monData.put("maMon", ct.getMonAn().getMaMon());
            monData.put("sl", ct.getSl());
            monData.put("donGiaBan", ct.getDonGiaBan());

            Map<String, String> thongTinMon = new HashMap<>();
            thongTinMon.put("tenMon", ct.getMonAn().getTenMon());
            monData.put("monAn", thongTinMon);

            dsMon.add(monData);
        }
        response.put("chiTietList", dsMon);

        return ResponseEntity.ok(response);
    }

    // 3. THANH TOÁN & TRỪ KHO TỰ ĐỘNG
    @PutMapping("/thanh-toan")
    @Transactional
    public ResponseEntity<?> thanhToan(@RequestBody Map<String, Object> payload) {
        try {
            String maHD = (String) payload.get("maHD");
            String maBan = (String) payload.get("maBan");
            String pttt = (String) payload.get("pttt");

            HoaDon hd = hoaDonRepository.findById(maHD)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

            hd.setPttt(pttt);
            hoaDonRepository.save(hd);

            if (maBan != null) {
                BanAn ban = banAnRepository.findById(maBan).orElse(null);
                if (ban != null) {
                    ban.setStatus("Trống");
                    banAnRepository.save(ban);
                }
            }

            // TRỪ KHO THEO CÔNG THỨC
            List<HdMa> chiTietList = hdMaRepository.findByHoaDon_MaHD(maHD); // Đã đổi class
            for (HdMa chiTiet : chiTietList) {
                String maMon = chiTiet.getMonAn().getMaMon();
                int soLuongMonBanRa = chiTiet.getSl();

                List<MaNl> congThuc = maNlRepository.findByMonAn_MaMon(maMon); // Đã đổi class

                for (MaNl ct : congThuc) {
                    String maNL = ct.getNguyenLieu().getMaNL();
                    Double dinhLuong = ct.getDinhLuong();

                    NguyenLieu kho = nguyenLieuRepository.findById(maNL).orElse(null);
                    if (kho != null) {
                        double tongTieuHao = dinhLuong * soLuongMonBanRa;
                        double slMoi = kho.getSl() - tongTieuHao;

                        kho.setSl(slMoi);
                        nguyenLieuRepository.save(kho);
                    }
                }
            }

            return ResponseEntity.ok("Thanh toán thành công và đã trừ kho tự động!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}