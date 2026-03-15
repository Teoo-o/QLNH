package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.MonAn;
import com.nhahang.webnhahang.repository.DanhMucRepository;
import com.nhahang.webnhahang.repository.MonAnRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MonAnController {

    private final MonAnRepository monAnRepository;
    private final DanhMucRepository danhMucRepository;

    public MonAnController(MonAnRepository monAnRepository, DanhMucRepository danhMucRepository) {
        this.monAnRepository = monAnRepository;
        this.danhMucRepository = danhMucRepository;
    }

    @GetMapping("/monan")
    public String hienThiTrangMonAn(Model model) {
        model.addAttribute("dsMonAn", monAnRepository.findAll());
        return "monan";
    }

    @GetMapping("/monan/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("monAnMoi", new MonAn());
        model.addAttribute("dsDanhMuc", danhMucRepository.findAll());
        return "themmonan";
    }

    @PostMapping("/monan/luu")
    public String luuMonAn(@ModelAttribute("monAnMoi") MonAn monAn) {
        monAnRepository.save(monAn);
        return "redirect:/monan";
    }

    @GetMapping("/monan/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer maMon, Model model) {
        model.addAttribute("monAnMoi", monAnRepository.findById(maMon).orElse(null));
        model.addAttribute("dsDanhMuc", danhMucRepository.findAll());
        return "themmonan";
    }

    @GetMapping("/monan/xoa/{id}")
    public String xoaMonAn(@PathVariable("id") Integer maMon) {
        monAnRepository.deleteById(maMon);
        return "redirect:/monan";
    }
}