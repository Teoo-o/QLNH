package com.nhahang.qlnh.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class HdMaId implements Serializable {
    private String hoaDon; // Tên biến phải khớp với tên biến map ở class HdMa
    private String monAn;
}