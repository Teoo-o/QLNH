package com.nhahang.qlnh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "HD_MA")
public class HdMa {

    @EmbeddedId
    private HdMaKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHD")
    @JoinColumn(name = "MaHD")
    @JsonIgnore // <--- KHIÊN BẢO VỆ CHẶN VÒNG LẶP JSON LÀ Ở ĐÂY
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maMon")
    @JoinColumn(name = "MaMon")
    private MonAn monAn;

    @Column(name = "SL")
    private Integer sl;

    @Column(name = "DonGiaBan")
    private Double donGiaBan;

    // --- Các hàm Get/Set của bạn giữ nguyên ---
    public HdMaKey getId() { return id; }
    public void setId(HdMaKey id) { this.id = id; }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) { this.hoaDon = hoaDon; }

    public MonAn getMonAn() { return monAn; }
    public void setMonAn(MonAn monAn) { this.monAn = monAn; }

    public Integer getSl() { return sl; }
    public void setSl(Integer sl) { this.sl = sl; }

    public Double getDonGiaBan() { return donGiaBan; }
    public void setDonGiaBan(Double donGiaBan) { this.donGiaBan = donGiaBan; }
}