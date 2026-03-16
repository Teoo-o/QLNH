package com.nhahang.webnhahang.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "KHUYEN_MAI")
@Data
public class KhuyenMai {
    @Id
    @Column(name = "MaKM")
    private String maKM;

    @Column(name = "TenKM")
    private String tenKM;

    @Column(name = "NgayBD")
    private LocalDateTime ngayBD;

    @Column(name = "PhanTramGiam")
    private Double phanTramGiam;

    @Column(name = "NgayKT")
    private LocalDateTime ngayKT;
}