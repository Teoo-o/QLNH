package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DANH_MUC")
@Data
public class DanhMuc {
    @Id
    @Column(name = "MaDM")
    private String maDM;

    @Column(name = "TenDM")
    private String tenDM;
}