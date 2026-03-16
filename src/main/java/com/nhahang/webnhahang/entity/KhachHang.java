package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "KHACH_HANG")
@Data
public class KhachHang {
    @Id
    @Column(name = "MaKH")
    private String maKH;

    @Column(name = "TenKH")
    private String tenKH;

    @Column(name = "SDT")
    private String sdt;
}