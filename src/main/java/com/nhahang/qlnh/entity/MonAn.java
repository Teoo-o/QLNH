package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "MON_AN")
@Data
public class MonAn {
    @Id
    @Column(name = "MaMon")
    private String maMon;

    @Column(name = "TenMon")
    private String tenMon;

    @Column(name = "DonGia")
    private Double donGia;

    @Column(name = "HinhAnh")
    private String hinhAnh;

    @Column(name = "DaXoa")
    private Boolean daXoa = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDM")
    @ToString.Exclude
    private DanhMuc danhMuc;
}