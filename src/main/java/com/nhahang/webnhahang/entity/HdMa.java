package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "HD_MA")
@IdClass(HdMaId.class) // Khai báo sử dụng khóa phức hợp
@Data
public class HdMa {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHD")
    private HoaDon hoaDon;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaMon")
    private MonAn monAn;

    @Column(name = "SL")
    private Integer sl;

    @Column(name = "DonGiaBan")
    private Double donGiaBan;
}