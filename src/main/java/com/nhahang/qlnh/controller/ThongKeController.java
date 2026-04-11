package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.HoaDon;
import com.nhahang.qlnh.repository.HdMaRepository;
import com.nhahang.qlnh.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/thongke")
@CrossOrigin(origins = "*")
public class ThongKeController {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private HdMaRepository hdMaRepository;

    @GetMapping("/tong-quan")
    public ResponseEntity<?> layThongKeTongQuan() {
        List<HoaDon> tatCaHoaDon = hoaDonRepository.findAll();
        double doanhThuHomNay = 0;
        int soDonHomNay = 0;
        LocalDate today = LocalDate.now();

        for (HoaDon hd : tatCaHoaDon) {
            if (hd.getPttt() != null && !hd.getPttt().equals("Chưa thanh toán") && hd.getNgayTao() != null) {
                if (hd.getNgayTao().toLocalDate().isEqual(today)) {
                    soDonHomNay++;

                    double tongMon = hdMaRepository.findByHoaDon_MaHD(hd.getMaHD())
                            .stream().mapToDouble(ct -> ct.getSl() * ct.getDonGiaBan()).sum();

                    double giamGia = hd.getSoTienGiam() != null ? hd.getSoTienGiam() : 0.0;
                    doanhThuHomNay += (tongMon - giamGia);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("doanhThuHomNay", doanhThuHomNay);
        result.put("soDonHomNay", soDonHomNay);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/bieu-do")
    public ResponseEntity<?> layDuLieuBieuDo() {
        List<HoaDon> tatCaHoaDon = hoaDonRepository.findAll();
        Map<String, Double> doanhThu7Ngay = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (int i = 6; i >= 0; i--) {
            doanhThu7Ngay.put(LocalDate.now().minusDays(i).format(formatter), 0.0);
        }

        for (HoaDon hd : tatCaHoaDon) {
            if (hd.getPttt() != null && !hd.getPttt().equals("Chưa thanh toán") && hd.getNgayTao() != null) {
                LocalDate ngayTao = hd.getNgayTao().toLocalDate();

                if (ngayTao.isAfter(LocalDate.now().minusDays(7))) {
                    String key = ngayTao.format(formatter);
                    if (doanhThu7Ngay.containsKey(key)) {
                        double tongMon = hdMaRepository.findByHoaDon_MaHD(hd.getMaHD())
                                .stream().mapToDouble(ct -> ct.getSl() * ct.getDonGiaBan()).sum();
                        double giamGia = hd.getSoTienGiam() != null ? hd.getSoTienGiam() : 0.0;

                        doanhThu7Ngay.put(key, doanhThu7Ngay.get(key) + (tongMon - giamGia));
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(doanhThu7Ngay.keySet()));
        result.put("data", new ArrayList<>(doanhThu7Ngay.values()));
        return ResponseEntity.ok(result);
    }
}