package com.nhahang.qlnh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class HdMaKey implements Serializable {

    @Column(name = "MaHD")
    private String maHD;

    @Column(name = "MaMon")
    private String maMon;

    // --- Constructor tự viết để vá lỗi "cannot be applied to given types" ---
    public HdMaKey() {
    }

    public HdMaKey(String maHD, String maMon) {
        this.maHD = maHD;
        this.maMon = maMon;
    }

    // --- Getters / Setters tự viết ---
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }
}