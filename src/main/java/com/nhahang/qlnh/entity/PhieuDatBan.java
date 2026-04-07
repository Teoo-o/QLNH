package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "PHIEU_DAT_BAN")
@Data
public class PhieuDatBan {
    @Id
    @Column(name = "MaPhieu")
    private String maPhieu;

    @Column(name = "ThoiGian")
    private LocalDateTime thoiGian;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "SoNguoi")
    private Integer soNguoi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaKH")
    @ToString.Exclude
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaBan")
    @ToString.Exclude
    private BanAn banAn;
}