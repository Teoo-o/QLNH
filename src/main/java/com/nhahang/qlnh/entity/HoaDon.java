package com.nhahang.qlnh.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOA_DON")
public class HoaDon {

    @Id
    @Column(name = "MaHD")
    private String maHD;

    @ManyToOne
    @JoinColumn(name = "MaBan")
    private BanAn banAn;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "LoaiHD")
    private String loaiHD;

    @Column(name = "PTTT")
    private String pttt;

    // --- CÁC CỘT MỚI ĐỂ LƯU KHUYẾN MÃI VÀ THÔNG TIN KHÁC ---
    @Column(name = "MaKM")
    private String maKM;

    @Column(name = "SoTienGiam")
    private Double soTienGiam;

    @Column(name = "PhiGiaoHang")
    private Double phiGiaoHang;

    @Column(name = "PhiNenTang")
    private Double phiNenTang;

    @Column(name = "MaNV")
    private String maNV;

    @Column(name = "MaKH")
    private String maKH;

    // --- GETTER & SETTER ---
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public BanAn getBanAn() { return banAn; }
    public void setBanAn(BanAn banAn) { this.banAn = banAn; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public String getLoaiHD() { return loaiHD; }
    public void setLoaiHD(String loaiHD) { this.loaiHD = loaiHD; }

    public String getPttt() { return pttt; }
    public void setPttt(String pttt) { this.pttt = pttt; }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public Double getSoTienGiam() { return soTienGiam; }
    public void setSoTienGiam(Double soTienGiam) { this.soTienGiam = soTienGiam; }

    public Double getPhiGiaoHang() { return phiGiaoHang; }
    public void setPhiGiaoHang(Double phiGiaoHang) { this.phiGiaoHang = phiGiaoHang; }

    public Double getPhiNenTang() { return phiNenTang; }
    public void setPhiNenTang(Double phiNenTang) { this.phiNenTang = phiNenTang; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
}