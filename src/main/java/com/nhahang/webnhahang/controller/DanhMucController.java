package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.DanhMuc;
import com.nhahang.webnhahang.repository.DanhMucRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class DanhMucController {

    private final DanhMucRepository danhMucRepository;

    public DanhMucController(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    @GetMapping("/danhmuc")
    public String hienThiTrangDanhMuc(Model model) {
        List<DanhMuc> danhSach = danhMucRepository.findAll();

        model.addAttribute("dsDanhMuc", danhSach);

        return "danhmuc";
    }
    @GetMapping("/danhmuc/them")
    public String hienThiFormThem(Model model) {
        DanhMuc danhMucTrang = new DanhMuc();
        model.addAttribute("danhMucMoi", danhMucTrang);
        return "themdanhmuc";
    }

    @PostMapping("/danhmuc/luu")
    public String luuDanhMuc(@ModelAttribute("danhMucMoi") DanhMuc danhMuc) {
        danhMucRepository.save(danhMuc);
        return "redirect:/danhmuc";
    }
    @GetMapping("/danhmuc/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer maDM, Model model) {
        DanhMuc danhMucCanSua = danhMucRepository.findById(maDM).orElse(null);

        model.addAttribute("danhMucMoi", danhMucCanSua);

        return "themdanhmuc";
    }

    @GetMapping("/danhmuc/xoa/{id}")
    public String xoaDanhMuc(@PathVariable("id") Integer maDM) {
        danhMucRepository.deleteById(maDM);
        return "redirect:/danhmuc";
    }
}