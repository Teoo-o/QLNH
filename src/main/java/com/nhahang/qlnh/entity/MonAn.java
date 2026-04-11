package com.nhahang.qlnh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MON_AN")
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
    private Boolean daXoa; // Thêm cột quản lý Thùng Rác

    // --- GETTER & SETTER ---
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public Boolean getDaXoa() { return daXoa; }
    public void setDaXoa(Boolean daXoa) { this.daXoa = daXoa; }
}