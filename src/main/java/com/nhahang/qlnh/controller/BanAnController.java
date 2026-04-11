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
            danhSachBan = banAnRepository.findByDaXoaFalse();
        } catch (Exception e) {
            danhSachBan = banAnRepository.findAll();
        }

        LocalDateTime now = LocalDateTime.now();
        List<PhieuDatBan> tatCaPhieu = phieuDatBanRepository.findAll();
        boolean coThayDoi = false;

        for (BanAn ban : danhSachBan) {
            boolean sapCoKhachDat = tatCaPhieu.stream().anyMatch(p ->
                    "Chờ nhận bàn".equals(p.getTrangThai()) &&
                            p.getBanAn() != null &&
                            p.getBanAn().getMaBan().equals(ban.getMaBan()) &&
                            p.getThoiGian().isBefore(now.plusHours(2)) &&
                            p.getThoiGian().isAfter(now.minusMinutes(15))
            );

            if (sapCoKhachDat) {
                if ("Trống".equals(ban.getStatus()) || ban.getStatus() == null) {
                    ban.setStatus("Đã đặt");
                    coThayDoi = true;
                }
            } else {
                if ("Đã đặt".equals(ban.getStatus())) {
                    ban.setStatus("Trống");
                    coThayDoi = true;
                }
            }
        }

        if (coThayDoi) {
            banAnRepository.saveAll(danhSachBan);
        }

        return danhSachBan;
    }
}