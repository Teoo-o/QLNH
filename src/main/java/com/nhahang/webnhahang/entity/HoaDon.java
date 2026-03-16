package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOA_DON")
@Data
public class HoaDon {
    @Id
    @Column(name = "MaHD")
    private String maHD;

    @Column(name = "LoaiHD")
    private String loaiHD;

    @Column(name = "PhiGiaoHang")
    private Double phiGiaoHang;

    @Column(name = "SoTienGiam")
    private Double soTienGiam;

    @Column(name = "PhiNenTang")
    private Double phiNenTang;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "PTTT")
    private String pttt;

    // --- BẮT ĐẦU MAPPING CÁC KHÓA NGOẠI ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKM")
    @ToString.Exclude
    private KhuyenMai khuyenMai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieu")
    @ToString.Exclude
    private PhieuDatBan phieuDatBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNV")
    @ToString.Exclude
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDT")
    @ToString.Exclude
    private DoiTacGiaoHang doiTacGiaoHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBan")
    @ToString.Exclude
    private BanAn banAn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKH")
    @ToString.Exclude
    private KhachHang khachHang;
}