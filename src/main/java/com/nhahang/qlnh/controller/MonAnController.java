package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.MonAn;
import com.nhahang.qlnh.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monan") // ĐÚNG CHUẨN ĐƯỜNG DẪN HTML ĐANG GỌI
@CrossOrigin(origins = "*")   // BÀ BẢO VỆ CHO PHÉP WEB ĐI QUA
public class MonAnController {

    @Autowired
    private MonAnRepository monAnRepository;

    // Lấy toàn bộ danh sách Món Ăn để hiển thị lên Menu Gọi Món
    @GetMapping
    public List<MonAn> layThucDon() {
        return monAnRepository.findAll();
    }
}   