package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.MonAn;
import com.nhahang.qlnh.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/monan")
@CrossOrigin(origins = "*")
public class MonAnController {

    @Autowired
    private MonAnRepository monAnRepository;

    // 1. Lấy Menu đang bán (Chưa bị xóa)
    @GetMapping
    public List<MonAn> layThucDon() {
        return monAnRepository.findAll().stream()
                .filter(mon -> mon.getDaXoa() == null || !mon.getDaXoa())
                .collect(Collectors.toList());
    }

    // 2. Lấy Menu trong Thùng Rác
    @GetMapping("/thung-rac")
    public List<MonAn> layThungRac() {
        return monAnRepository.findAll().stream()
                .filter(mon -> mon.getDaXoa() != null && mon.getDaXoa())
                .collect(Collectors.toList());
    }

    // 3. Thêm mới Món ăn
    @PostMapping("/tao-moi")
    public ResponseEntity<?> taoMoi(@RequestBody MonAn monAn) {
        try {
            if (monAnRepository.existsById(monAn.getMaMon())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã món ăn đã tồn tại!"));
            }
            monAn.setDaXoa(false); // Mặc định là không xóa
            monAnRepository.save(monAn);
            return ResponseEntity.ok(Collections.singletonMap("message", "Thêm món ăn thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi: " + e.getMessage()));
        }
    }

    // 4. Sửa Món ăn
    @PutMapping("/sua/{maMon}")
    public ResponseEntity<?> suaMonAn(@PathVariable String maMon, @RequestBody MonAn thongTinMoi) {
        return monAnRepository.findById(maMon).map(mon -> {
            mon.setTenMon(thongTinMoi.getTenMon());
            mon.setDonGia(thongTinMoi.getDonGia());
            mon.setHinhAnh(thongTinMoi.getHinhAnh());
            monAnRepository.save(mon);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy món ăn!")));
    }

    // 5. XÓA MỀM (Đưa vào Thùng Rác)
    @DeleteMapping("/xoa/{maMon}")
    public ResponseEntity<?> xoaMonAn(@PathVariable String maMon) {
        return monAnRepository.findById(maMon).map(mon -> {
            mon.setDaXoa(true);
            monAnRepository.save(mon);
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã đưa món ăn vào Thùng rác (Ngừng bán)!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy món ăn!")));
    }

    // 6. KHÔI PHỤC TỪ THÙNG RÁC
    @PutMapping("/khoi-phuc/{maMon}")
    public ResponseEntity<?> khoiPhucMonAn(@PathVariable String maMon) {
        return monAnRepository.findById(maMon).map(mon -> {
            mon.setDaXoa(false);
            monAnRepository.save(mon);
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã khôi phục món ăn trở lại Menu!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy món ăn!")));
    }
}