package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.BanAn;
import com.nhahang.webnhahang.repository.BanAnRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BanAnController {

    private final BanAnRepository banAnRepository;

    public BanAnController(BanAnRepository banAnRepository) {
        this.banAnRepository = banAnRepository;
    }

    @GetMapping("/banan")
    public String hienThiTrangBanAn(Model model) {
        model.addAttribute("dsBanAn", banAnRepository.findAll());
        return "banan";
    }

    @GetMapping("/banan/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("banAnMoi", new BanAn());
        return "thembanan";
    }

    @PostMapping("/banan/luu")
    public String luuBanAn(@ModelAttribute("banAnMoi") BanAn banAn) {
        banAnRepository.save(banAn);
        return "redirect:/banan";
    }

    @GetMapping("/banan/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer maBan, Model model) {
        model.addAttribute("banAnMoi", banAnRepository.findById(maBan).orElse(null));
        return "thembanan";
    }

    @GetMapping("/banan/xoa/{id}")
    public String xoaBanAn(@PathVariable("id") Integer maBan) {
        banAnRepository.deleteById(maBan);
        return "redirect:/banan";
    }
}