package com.nhahang.qlnh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "KHUYEN_MAI")
public class KhuyenMai {

    @Id
    @Column(name = "MaKM")
    private String maKM;

    @Column(name = "TenKM")
    private String tenKM;

    @Column(name = "NgayBD")
    private LocalDateTime ngayBD;

    @Column(name = "NgayKT")
    private LocalDateTime ngayKT;

    @Column(name = "PhanTramGiam")
    private Double phanTramGiam; // Ví dụ: 10% thì lưu là 10.0

    // --- GETTER & SETTER ---
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public LocalDateTime getNgayBD() { return ngayBD; }
    public void setNgayBD(LocalDateTime ngayBD) { this.ngayBD = ngayBD; }

    public LocalDateTime getNgayKT() { return ngayKT; }
    public void setNgayKT(LocalDateTime ngayKT) { this.ngayKT = ngayKT; }

    public Double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(Double phanTramGiam) { this.phanTramGiam = phanTramGiam; }
}