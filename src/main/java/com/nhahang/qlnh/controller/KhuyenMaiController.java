package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.KhuyenMai;
import com.nhahang.qlnh.repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/khuyenmai")
@CrossOrigin(origins = "*")
public class KhuyenMaiController {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @GetMapping
    public List<KhuyenMai> layDanhSach() {
        return khuyenMaiRepository.findAll();
    }

    @PostMapping("/tao-moi")
    public ResponseEntity<?> taoMoi(@RequestBody KhuyenMai km) {
        try {
            if(khuyenMaiRepository.existsById(km.getMaKM())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã khuyến mãi này đã tồn tại!"));
            }
            khuyenMaiRepository.save(km);
            return ResponseEntity.ok(Collections.singletonMap("message", "Tạo mã khuyến mãi thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi: " + e.getMessage()));
        }
    }

    @DeleteMapping("/xoa/{maKM}")
    public ResponseEntity<?> xoaMa(@PathVariable String maKM) {
        if(khuyenMaiRepository.existsById(maKM)) {
            khuyenMaiRepository.deleteById(maKM);
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã xóa mã khuyến mãi!"));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy mã này!"));
    }

    @GetMapping("/kiem-tra/{maKM}")
    public ResponseEntity<?> kiemTraMa(@PathVariable String maKM) {
        return khuyenMaiRepository.timMaHopLe(maKM, LocalDateTime.now())
                .map(km -> ResponseEntity.ok(km))
                .orElse(ResponseEntity.badRequest().body(null));
    }
}