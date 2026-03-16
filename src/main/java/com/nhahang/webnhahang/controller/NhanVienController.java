package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.NhanVien;
import com.nhahang.webnhahang.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/nhanvien")
@CrossOrigin(origins = "*")
public class NhanVienController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @PostMapping("/login")
    public ResponseEntity<?> dangNhap(@RequestBody Map<String, String> loginData) {
        String taiKhoan = loginData.get("taiKhoan");
        String matKhau = loginData.get("matKhau");

        NhanVien nv = nhanVienRepository.findByTaiKhoanAndMatKhau(taiKhoan, matKhau);

        if (nv != null) {
            return ResponseEntity.ok(nv); // Trả về thông tin nhân viên (kèm Chức vụ)
        } else {
            return ResponseEntity.status(401).body("Sai tài khoản hoặc mật khẩu!");
        }
    }
}