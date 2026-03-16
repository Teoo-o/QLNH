package com.nhahang.webnhahang.dto;

import lombok.Data;
import java.util.List;

@Data
public class HoaDonRequest {
    private String loaiHD;
    private String pttt;
    private List<ChiTietMon> chiTietList;

    @Data
    public static class ChiTietMon {
        private String maMon;
        private Integer soLuong;
        private Double dongGia;
    }
}