package com.nhahang.qlnh.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MA_NL")
public class MaNl {

    @EmbeddedId
    private MaNlKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maMon")
    @JoinColumn(name = "MaMon")
    private MonAn monAn;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maNL")
    @JoinColumn(name = "MaNL")
    private NguyenLieu nguyenLieu;

    @Column(name = "DinhLuong")
    private Double dinhLuong;

    // --- Vá toàn bộ lỗi "cannot find symbol method" ---
    public MaNlKey getId() { return id; }
    public void setId(MaNlKey id) { this.id = id; }

    public MonAn getMonAn() { return monAn; }
    public void setMonAn(MonAn monAn) { this.monAn = monAn; }

    public NguyenLieu getNguyenLieu() { return nguyenLieu; }
    public void setNguyenLieu(NguyenLieu nguyenLieu) { this.nguyenLieu = nguyenLieu; }

    public Double getDinhLuong() { return dinhLuong; }
    public void setDinhLuong(Double dinhLuong) { this.dinhLuong = dinhLuong; }
}