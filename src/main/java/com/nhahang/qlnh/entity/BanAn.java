package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "BAN_AN")
@Data
public class BanAn {
    @Id
    @Column(name = "MaBan")
    private String maBan;

    @Column(name = "KhuVuc")
    private String khuVuc;

    @Column(name = "SoGhe")
    private Integer soGhe;

    @Column(name = "Status")
    private String status;

    @Column(name = "DaXoa")
    private Boolean daXoa = false;

    // Viết luôn Get/Set cho chắc cú
    public Boolean getDaXoa() {
        return daXoa;
    }

    public void setDaXoa(Boolean daXoa) {
        this.daXoa = daXoa;
    }
}