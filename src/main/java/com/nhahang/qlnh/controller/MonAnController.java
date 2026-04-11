package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.DanhMuc;
import com.nhahang.qlnh.entity.MonAn;
import com.nhahang.qlnh.repository.DanhMucRepository;
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

    @Autowired private MonAnRepository monAnRepository;
    @Autowired private DanhMucRepository danhMucRepository;

    @GetMapping
    public List<MonAn> layThucDon() {
        return monAnRepository.findAll().stream()
                .filter(mon -> mon.getDaXoa() == null || !mon.getDaXoa())
                .collect(Collectors.toList());
    }

    @GetMapping("/thung-rac")
    public List<MonAn> layThungRac() {
        return monAnRepository.findAll().stream()
                .filter(mon -> mon.getDaXoa() != null && mon.getDaXoa())
                .collect(Collectors.toList());
    }

    @PostMapping("/tao-moi")
    public ResponseEntity<?> taoMoi(@RequestBody MonAn payload) {
        try {
            if (monAnRepository.existsById(payload.getMaMon())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã món ăn đã tồn tại!"));
            }

            if (payload.getDanhMuc() != null && payload.getDanhMuc().getMaDM() != null) {
                DanhMuc dm = danhMucRepository.findById(payload.getDanhMuc().getMaDM()).orElse(null);
                payload.setDanhMuc(dm);
            }

            payload.setDaXoa(false);
            monAnRepository.save(payload);
            return ResponseEntity.ok(Collections.singletonMap("message", "Thêm món thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi: " + e.getMessage()));
        }
    }

    @PutMapping("/sua/{maMon}")
    public ResponseEntity<?> suaMonAn(@PathVariable String maMon, @RequestBody MonAn payload) {
        return monAnRepository.findById(maMon).map(mon -> {
            mon.setTenMon(payload.getTenMon());
            mon.setDonGia(payload.getDonGia());
            mon.setHinhAnh(payload.getHinhAnh());

            if (payload.getDanhMuc() != null && payload.getDanhMuc().getMaDM() != null) {
                DanhMuc dm = danhMucRepository.findById(payload.getDanhMuc().getMaDM()).orElse(null);
                mon.setDanhMuc(dm);
            } else {
                mon.setDanhMuc(null);
            }

            monAnRepository.save(mon);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật món thành công!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy món!")));
    }

    @DeleteMapping("/xoa/{maMon}")
    public ResponseEntity<?> xoaMonAn(@PathVariable String maMon) {
        return monAnRepository.findById(maMon).map(mon -> {
            mon.setDaXoa(true);
            monAnRepository.save(mon);
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã đưa vào thùng rác!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi!")));
    }

    @PutMapping("/khoi-phuc/{maMon}")
    public ResponseEntity<?> khoiPhucMonAn(@PathVariable String maMon) {
        return monAnRepository.findById(maMon).map(mon -> {
            mon.setDaXoa(false);
            monAnRepository.save(mon);
            return ResponseEntity.ok(Collections.singletonMap("message", "Đã khôi phục!"));
        }).orElse(ResponseEntity.badRequest().body(Collections.singletonMap("message", "Lỗi!")));
    }
}