package com.nhahang.webnhahang.controller;

import com.nhahang.webnhahang.dto.HoaDonRequest;
import com.nhahang.webnhahang.entity.HoaDon;
import com.nhahang.webnhahang.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin(origins = "*")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/tinh-tien/{maHD}")
    public ResponseEntity<HoaDon> chotHoaDon(@PathVariable String maHD) {
        try {
            HoaDon hoaDonDaTinh = hoaDonService.tinhToanVaChotHoaDon(maHD);
            return ResponseEntity.ok(hoaDonDaTinh);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/tao-moi")
    public ResponseEntity<HoaDon> taoHoaDon(@RequestBody HoaDonRequest request) {
        try {
            HoaDon hdMoi = hoaDonService.taoHoaDonMoi(request);
            return ResponseEntity.ok(hdMoi);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}