package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.BanAn;
import com.nhahang.qlnh.entity.PhieuDatBan;
import com.nhahang.qlnh.repository.BanAnRepository;
import com.nhahang.qlnh.repository.PhieuDatBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datban")
@CrossOrigin(origins = "*")
public class PhieuDatBanController {

    @Autowired
    private PhieuDatBanRepository phieuDatBanRepository;

    // Phải gọi thêm kho Bàn Ăn để cập nhật trạng thái bàn
    @Autowired
    private BanAnRepository banAnRepository;

    // Lấy toàn bộ danh sách phiếu đặt bàn
    @GetMapping
    public List<PhieuDatBan> layDanhSachDatBan() {
        return phieuDatBanRepository.findAll();
    }

    // 1. Xử lý khách đến Nhận Bàn
    @PutMapping("/nhan-ban/{maPhieu}")
    public ResponseEntity<?> khachNhanBan(@PathVariable String maPhieu) {
        return phieuDatBanRepository.findById(maPhieu).map(phieu -> {
            // Đổi trạng thái phiếu
            phieu.setTrangThai("Đã nhận bàn");
            phieuDatBanRepository.save(phieu);

            // Tự động chuyển trạng thái Bàn Ăn thành "Có khách"
            if (phieu.getBanAn() != null) {
                BanAn ban = phieu.getBanAn();
                ban.setStatus("Có khách");
                banAnRepository.save(ban);
            }

            return ResponseEntity.ok("Nhận bàn thành công! Sơ đồ bàn đã cập nhật thành 'Có khách'.");
        }).orElse(ResponseEntity.badRequest().body("Không tìm thấy phiếu đặt bàn này!"));
    }

    // 2. Xử lý khách Hủy đặt bàn
    @PutMapping("/huy/{maPhieu}")
    public ResponseEntity<?> khachHuyBan(@PathVariable String maPhieu) {
        return phieuDatBanRepository.findById(maPhieu).map(phieu -> {
            // Đổi trạng thái phiếu
            phieu.setTrangThai("Đã hủy");
            phieuDatBanRepository.save(phieu);

            // Tự động giải phóng Bàn Ăn về trạng thái "Trống"
            if (phieu.getBanAn() != null) {
                BanAn ban = phieu.getBanAn();
                ban.setStatus("Trống");
                banAnRepository.save(ban);
            }

            return ResponseEntity.ok("Đã hủy phiếu đặt bàn! Bàn đã được giải phóng thành 'Trống'.");
        }).orElse(ResponseEntity.badRequest().body("Không tìm thấy phiếu đặt bàn này!"));
    }
}