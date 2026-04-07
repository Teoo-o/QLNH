package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NGUYEN_LIEU")
@Data
public class NguyenLieu {
    @Id
    @Column(name = "MaNL")
    private String maNL;

    @Column(name = "TenNL")
    private String tenNL;

    @Column(name = "DonViTinh")
    private String donViTinh;

    @Column(name = "SL")
    private Double sl;
}