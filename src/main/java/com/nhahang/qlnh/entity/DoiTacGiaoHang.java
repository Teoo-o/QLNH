package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DOI_TAC_GIAO_HANG")
@Data
public class DoiTacGiaoHang {
    @Id
    @Column(name = "MaDT")
    private String maDT;

    @Column(name = "TenDT")
    private String tenDT;

    @Column(name = "PhanTramNenTang")
    private Double phanTramNenTang;
}