package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.BanAn;
import com.nhahang.qlnh.repository.BanAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banan")
@CrossOrigin(origins = "*")
public class BanAnController {

    @Autowired
    private BanAnRepository banAnRepository;

    @GetMapping
    public List<BanAn> layDanhSachBan() {
        return banAnRepository.findByDaXoaFalse();
    }
}