package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MA_NL")
@IdClass(MaNlId.class)
@Data
public class MaNl {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaMon")
    private MonAn monAn;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNL")
    private NguyenLieu nguyenLieu;

    @Column(name = "DinhLuong")
    private Double dinhLuong;
}