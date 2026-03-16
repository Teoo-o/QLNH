package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.PhieuDatBan;
import com.nhahang.webnhahang.repository.PhieuDatBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/datban")
@CrossOrigin(origins = "*")
public class PhieuDatBanController {

    @Autowired
    private PhieuDatBanRepository phieuDatBanRepository;

    @GetMapping
    public List<PhieuDatBan> layDanhSachDatBan() {
        return phieuDatBanRepository.findAll();
    }
}