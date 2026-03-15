package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOA_DON")
@Data
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHD")
    private Integer maHD;

    @ManyToOne
    @JoinColumn(name = "MaBan")
    private BanAn banAn;

    @Column(name = "NgayGioTao")
    private LocalDateTime ngayGioTao = LocalDateTime.now();

    @Column(name = "TrangThai")
    private String trangThai; // "Chưa thanh toán" hoặc "Đã thanh toán"

    @Column(name = "TongTien")
    private Double tongTien = 0.0;
}