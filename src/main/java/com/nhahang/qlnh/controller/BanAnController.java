package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.BanAn;
import com.nhahang.qlnh.entity.PhieuDatBan;
import com.nhahang.qlnh.repository.BanAnRepository;
import com.nhahang.qlnh.repository.PhieuDatBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/banan")
@CrossOrigin(origins = "*")
public class BanAnController {

    @Autowired
    private BanAnRepository banAnRepository;

    @Autowired
    private PhieuDatBanRepository phieuDatBanRepository;

    @GetMapping
    public List<BanAn> layDanhSachBan() {
        List<BanAn> danhSachBan;
        try {
            // Lấy danh sách bàn chưa bị xóa mềm
            danhSachBan = banAnRepository.findByDaXoaFalse();
        } catch (Exception e) {
            danhSachBan = banAnRepository.findAll();
        }

        LocalDateTime now = LocalDateTime.now();
        List<PhieuDatBan> tatCaPhieu = phieuDatBanRepository.findAll();
        boolean coThayDoi = false;

        for (BanAn ban : danhSachBan) {
            // Kiểm tra: Bàn này có khách đặt trong vòng 2 TIẾNG TỚI không?
            boolean sapCoKhachDat = tatCaPhieu.stream().anyMatch(p ->
                    "Chờ nhận bàn".equals(p.getTrangThai()) &&
                            p.getBanAn() != null &&
                            p.getBanAn().getMaBan().equals(ban.getMaBan()) &&
                            p.getThoiGian().isBefore(now.plusHours(2)) && // Nhỏ hơn 2 tiếng tới
                            p.getThoiGian().isAfter(now.minusMinutes(15)) // Chưa bị hệ thống hủy do lố 15 phút
            );

            if (sapCoKhachDat) {
                // CHỈ KHÓA BÀN KHI ĐANG TRỐNG
                // Nếu bàn "Có khách", hệ thống kệ cho khách ăn xong thanh toán mới khóa
                if ("Trống".equals(ban.getStatus()) || ban.getStatus() == null) {
                    ban.setStatus("Đã đặt");
                    coThayDoi = true;
                }
            } else {
                // Thời gian còn HƠN 2 TIẾNG -> Mở khóa để đón khách vãng lai
                if ("Đã đặt".equals(ban.getStatus())) {
                    ban.setStatus("Trống");
                    coThayDoi = true;
                }
            }
        }

        // Lưu đồng loạt nếu có bàn đổi màu
        if (coThayDoi) {
            banAnRepository.saveAll(danhSachBan);
        }

        return danhSachBan;
    }
}