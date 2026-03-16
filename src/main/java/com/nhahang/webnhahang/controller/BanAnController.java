package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.BanAn;
import com.nhahang.webnhahang.repository.BanAnRepository;
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

    // Lấy toàn bộ danh sách bàn ăn và trạng thái hiện tại
    @GetMapping
    public List<BanAn> layDanhSachBan() {
        return banAnRepository.findAll();
    }
}