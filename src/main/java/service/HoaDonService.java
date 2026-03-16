package com.nhahang.webnhahang.service;

import com.nhahang.webnhahang.entity.*;
import com.nhahang.webnhahang.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.nhahang.webnhahang.dto.HoaDonRequest;

@Service
public class HoaDonService {

    @Autowired
    private MonAnRepository monAnRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HdMaRepository hdMaRepository;

    @Autowired
    private MaNlRepository maNlRepository; // Gọi kho Công thức

    @Autowired
    private NguyenLieuRepository nguyenLieuRepository; // Gọi kho Nguyên liệu

    // Thêm @Transactional để lỡ đang trừ kho mà lỗi thì Rollback (hoàn tác) lại toàn bộ
    @Transactional
    public HoaDon tinhToanVaChotHoaDon(String maHD) {
        HoaDon hoaDon = hoaDonRepository.findById(maHD)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn: " + maHD));

        List<HdMa> danhSachMon = hdMaRepository.findByHoaDon(hoaDon);

        // 1. Tính tổng tiền món ăn
        double tongTienMon = 0.0;
        for (HdMa chiTiet : danhSachMon) {
            tongTienMon += chiTiet.getSl() * chiTiet.getDonGiaBan();
        }

        // 2. Tính tiền Khuyến Mãi
        if (hoaDon.getKhuyenMai() != null) {
            double tienGiam = tongTienMon * (hoaDon.getKhuyenMai().getPhanTramGiam() / 100.0);
            hoaDon.setSoTienGiam(tienGiam);
        } else {
            hoaDon.setSoTienGiam(0.0);
        }

        // 3. Tính phí Nền Tảng (Grab, Shopee)
        if (hoaDon.getDoiTacGiaoHang() != null) {
            double phiNenTang = tongTienMon * (hoaDon.getDoiTacGiaoHang().getPhanTramNenTang() / 100.0);
            hoaDon.setPhiNenTang(phiNenTang);
        } else {
            hoaDon.setPhiNenTang(0.0);
        }

        // 4. Chuyển trạng thái sang Đã thanh toán
        if(hoaDon.getPttt() == null || hoaDon.getPttt().equals("Chưa thanh toán")) {
            hoaDon.setPttt("Tiền mặt");
        }

        // ---------------------------------------------------------
        // 5. LOGIC TRỪ KHO NGUYÊN LIỆU
        // ---------------------------------------------------------
        for (HdMa chiTiet : danhSachMon) {
            MonAn monAn = chiTiet.getMonAn();
            int soLuongMonKhachGoi = chiTiet.getSl();

            // Tìm công thức nấu món này (Ví dụ: 1 Phở = 0.15kg Bò + 0.2kg Bánh phở)
            List<MaNl> congThuc = maNlRepository.findByMonAn(monAn);

            for (MaNl thanhPhan : congThuc) {
                NguyenLieu nl = thanhPhan.getNguyenLieu();

                // Tổng nguyên liệu cần dùng = Số món khách gọi * Định lượng 1 món
                double soLuongCanDung = soLuongMonKhachGoi * thanhPhan.getDinhLuong();

                // Kiểm tra xem kho còn đủ hàng không?
                if (nl.getSl() < soLuongCanDung) {
                    throw new RuntimeException("Cảnh báo: Kho không đủ [" + nl.getTenNL() + "] để làm món [" + monAn.getTenMon() + "]");
                }

                // Đủ hàng thì tiến hành trừ kho và lưu lại
                nl.setSl(nl.getSl() - soLuongCanDung);
                nguyenLieuRepository.save(nl);
            }
        }

        // 6. Lưu toàn bộ hóa đơn đã tính toán
        return hoaDonRepository.save(hoaDon);
    }
    @Transactional
    public HoaDon taoHoaDonMoi(HoaDonRequest request) {
        // 1. Tự động sinh mã Hóa Đơn mới
        String maHD = "HD" + (System.currentTimeMillis() % 10000);

        // 2. Tạo tờ Hóa Đơn và lưu xuống Database
        HoaDon hd = new HoaDon();
        hd.setMaHD(maHD);
        hd.setLoaiHD(request.getLoaiHD());
        hd.setPttt(request.getPttt());
        hd.setNgayTao(java.time.LocalDateTime.now());
        hoaDonRepository.save(hd);

        // 3. Quét từng món trong giỏ hàng để lưu vào bảng Chi Tiết (HD_MA)
        for (HoaDonRequest.ChiTietMon item : request.getChiTietList()) {
            MonAn monAn = monAnRepository.findById(item.getMaMon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món: " + item.getMaMon()));

            HdMa hdMa = new HdMa();
            hdMa.setHoaDon(hd);
            hdMa.setMonAn(monAn);
            hdMa.setSl(item.getSoLuong());
            hdMa.setDonGiaBan(item.getDongGia());

            hdMaRepository.save(hdMa);
        }
        return hd;
    }
}