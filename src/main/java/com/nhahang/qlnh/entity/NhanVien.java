package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NHAN_VIEN")
@Data
public class NhanVien {
    @Id
    @Column(name = "MaNV")
    private String maNV;

    @Column(name = "TenNV")
    private String tenNV;

    @Column(name = "ChucVu")
    private String chucVu;

    @Column(name = "TaiKhoan")
    private String taiKhoan;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "DaXoa")
    private Boolean daXoa = false;
}