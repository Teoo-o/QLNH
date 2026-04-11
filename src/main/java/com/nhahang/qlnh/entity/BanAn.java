package com.nhahang.qlnh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BAN_AN")
public class BanAn {

    @Id
    @Column(name = "MaBan")
    private String maBan;

    @Column(name = "SoGhe")
    private Integer soGhe;

    @Column(name = "Status")
    private String status;

    @Column(name = "DaXoa")
    private Boolean daXoa;

    @Column(name = "KhuVuc")
    private String khuVuc;


    public String getMaBan() { return maBan; }
    public void setMaBan(String maBan) { this.maBan = maBan; }

    public Integer getSoGhe() { return soGhe; }
    public void setSoGhe(Integer soGhe) { this.soGhe = soGhe; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getDaXoa() { return daXoa; }
    public void setDaXoa(Boolean daXoa) { this.daXoa = daXoa; }

    public String getKhuVuc() { return khuVuc; }
    public void setKhuVuc(String khuVuc) { this.khuVuc = khuVuc; }
}