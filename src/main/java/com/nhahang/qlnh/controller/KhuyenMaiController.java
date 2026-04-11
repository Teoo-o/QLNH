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

    // 1. Lấy danh sách toàn bộ khuyến mãi
    @GetMapping
    public List<KhuyenMai> layDanhSach() {
        return khuyenMaiRepository.findAll();
    }

    // 2. Thêm mới một mã khuyến mãi
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

    // 3. Xóa mã khuyến mãi
    @DeleteMapping("/xoa/{maKM}")
    public ResponseEntity<?> xoaMa(@PathVariable String maKM) {
        if(khuyenMaiRepository.existsById(maKM)) {
            khuyenMaiRepository.deleteById(maKM);
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã xóa mã khuyến mãi!"));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy mã này!"));
    }

    // 4. API Dành cho trang Thanh Toán: Kiểm tra mã hợp lệ không?
    @GetMapping("/kiem-tra/{maKM}")
    public ResponseEntity<?> kiemTraMa(@PathVariable String maKM) {
        return khuyenMaiRepository.timMaHopLe(maKM, LocalDateTime.now())
                .map(km -> ResponseEntity.ok(km)) // Hợp lệ -> Trả về thông tin KM để tính tiền
                .orElse(ResponseEntity.badRequest().body(null)); // Không hợp lệ / Hết hạn
    }
}