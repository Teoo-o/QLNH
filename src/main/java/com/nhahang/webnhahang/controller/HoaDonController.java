package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.entity.*;
import com.nhahang.webnhahang.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HoaDonController {

    // Khai báo đủ 4 cái kho dữ liệu
    private final HoaDonRepository hoaDonRepository;
    private final BanAnRepository banAnRepository;
    private final MonAnRepository monAnRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;

    public HoaDonController(HoaDonRepository hoaDonRepository, BanAnRepository banAnRepository,
                            MonAnRepository monAnRepository, ChiTietHoaDonRepository chiTietHoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.banAnRepository = banAnRepository;
        this.monAnRepository = monAnRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
    }

    @GetMapping("/hoadon")
    public String hienThiTrangHoaDon(Model model) {
        model.addAttribute("dsHoaDon", hoaDonRepository.findAll());
        model.addAttribute("dsBanAn", banAnRepository.findAll());
        return "hoadon";
    }

    @PostMapping("/hoadon/tao")
    public String taoHoaDonMoi(@RequestParam("maBan") Integer maBan) {
        BanAn ban = banAnRepository.findById(maBan).orElse(null);
        if (ban != null && "Trống".equals(ban.getTrangThai())) {
            HoaDon hdMoi = new HoaDon();
            hdMoi.setBanAn(ban);
            hdMoi.setNgayGioTao(LocalDateTime.now());
            hdMoi.setTrangThai("Chưa thanh toán");
            hdMoi.setTongTien(0.0);
            hoaDonRepository.save(hdMoi);

            ban.setTrangThai("Có khách");
            banAnRepository.save(ban);
        }
        return "redirect:/hoadon";
    }

    // Mở giao diện xem tờ hóa đơn và menu gọi món
    @GetMapping("/hoadon/chitiet/{id}")
    public String xemChiTietHoaDon(@PathVariable("id") Integer maHD, Model model) {
        HoaDon hoaDon = hoaDonRepository.findById(maHD).orElse(null);
        if (hoaDon == null) return "redirect:/hoadon";

        // Lấy danh sách các món khách ĐÃ GỌI
        List<ChiTietHoaDon> dsMonDaGoi = chiTietHoaDonRepository.findByHoaDon(hoaDon);

        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("dsMonDaGoi", dsMonDaGoi);
        model.addAttribute("dsMonAn", monAnRepository.findAll()); // Mang menu ra cho khách chọn

        return "chitiethoadon";
    }

    // Xử lý khi nhân viên bấm "Thêm vào bàn"
    @PostMapping("/hoadon/them-mon")
    public String themMonVaoHoaDon(@RequestParam("maHD") Integer maHD,
                                   @RequestParam("maMon") Integer maMon,
                                   @RequestParam("soLuong") Integer soLuong) {
        HoaDon hoaDon = hoaDonRepository.findById(maHD).orElse(null);
        MonAn monAn = monAnRepository.findById(maMon).orElse(null);

        if (hoaDon != null && monAn != null && soLuong > 0) {
            // 1. Tạo 1 dòng chi tiết hóa đơn (Ghi vào sổ)
            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
            chiTiet.setHoaDon(hoaDon);
            chiTiet.setMonAn(monAn);
            chiTiet.setSoLuong(soLuong);
            chiTiet.setDonGia(monAn.getDonGia()); // CHỐT GIÁ BÁN TẠI ĐÂY
            chiTietHoaDonRepository.save(chiTiet);

            // 2. Cộng tiền vào tổng của tờ Hóa Đơn
            Double tongTienCu = hoaDon.getTongTien() != null ? hoaDon.getTongTien() : 0.0;
            Double thanhTienMonNay = monAn.getDonGia() * soLuong;
            hoaDon.setTongTien(tongTienCu + thanhTienMonNay);
            hoaDonRepository.save(hoaDon);
        }

        return "redirect:/hoadon/chitiet/" + maHD; // Gọi xong thì F5 lại trang chi tiết đó
    }
    @PostMapping("/hoadon/thanhtoan")
    public String thanhToanHoaDon(@RequestParam("maHD") Integer maHD) {
        HoaDon hd = hoaDonRepository.findById(maHD).orElse(null);

        //Chỉ cho phép thanh toán nếu hóa đơn đang "Chưa thanh toán"
        if (hd != null && "Chưa thanh toán".equals(hd.getTrangThai())) {

            // 1. Chốt đơn: Đổi trạng thái Hóa Đơn
            hd.setTrangThai("Đã thanh toán");
            hoaDonRepository.save(hd);

            // 2. Trả lại Bàn Ăn về trạng thái "Trống"
            BanAn ban = hd.getBanAn();
            if (ban != null) {
                ban.setTrangThai("Trống");
                banAnRepository.save(ban);
            }
        }

        // Thanh toán xong thì quay về trang danh sách hóa đơn
        return "redirect:/hoadon";
    }
}