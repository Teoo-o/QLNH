package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "BAN_AN")
@Data
public class BanAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBan")
    private Integer maBan;

    @Column(name = "TenBan")
    private String tenBan;

    @Column(name = "TrangThai")
    private String trangThai; // "Trống", "Có khách", "Đã đặt"
}