package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.MonAn;
import com.nhahang.webnhahang.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monan") // Đường dẫn chuẩn khớp với code HTML
@CrossOrigin(origins = "*")   // Mở cửa cho phép HTML lấy dữ liệu
public class MonAnController {

    @Autowired
    private MonAnRepository monAnRepository;

    @GetMapping
    public List<MonAn> layTatCaMonAn() {
        return monAnRepository.findAll();
    }
}