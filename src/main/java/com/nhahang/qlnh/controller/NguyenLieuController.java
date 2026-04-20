package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.NguyenLieu;
import com.nhahang.qlnh.repository.NguyenLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kho")
@CrossOrigin(origins = "*")
public class NguyenLieuController {

    @Autowired
    private NguyenLieuRepository nguyenLieuRepository;

    @GetMapping
    public List<NguyenLieu> xemKho() {
        return nguyenLieuRepository.findAll();
    }

    @PutMapping("/nhap-them/{maNL}")
    public ResponseEntity<?> nhapThem(@PathVariable String maNL, @RequestBody Map<String, Double> payload) {
        return nguyenLieuRepository.findById(maNL).map(nl -> {
            Double soLuongThem = payload.get("soLuong");
            if (soLuongThem == null || soLuongThem <= 0) {
                return ResponseEntity.badRequest().body("Số lượng nhập phải lớn hơn 0!");
            }

            double slTong = nl.getSl() + soLuongThem;
            nl.setSl(Math.round(slTong * 100.0) / 100.0);
            nguyenLieuRepository.save(nl);

            return ResponseEntity.ok("✅ Đã nhập thành công " + soLuongThem + " " + nl.getDonViTinh() + " [" + nl.getTenNL() + "] vào kho!");
        }).orElse(ResponseEntity.badRequest().body("❌ Không tìm thấy nguyên liệu!"));
    }
}