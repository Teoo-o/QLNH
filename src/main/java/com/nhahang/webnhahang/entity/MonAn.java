package com.nhahang.webnhahang.entity;

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

    @Column(name = "DongGia")
    private Double dongGia;

    @Column(name = "HinhAnh")
    private String hinhAnh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDM")
    @ToString.Exclude
    private DanhMuc danhMuc;
}