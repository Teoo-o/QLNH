package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CHI_TIET_HOA_DON")
@Data
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTHD")
    private Integer maCTHD;

    @ManyToOne
    @JoinColumn(name = "MaHD")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "MaMon")
    private MonAn monAn;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGiaBatTu")
    private Double donGia;
}