package com.nhahang.qlnh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DANH_MUC")
public class DanhMuc {
    @Id
    @Column(name = "MaDM")
    private String maDM;

    @Column(name = "TenDM")
    private String tenDM;

    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }

    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }
}