package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.ChiTietHoaDon;
import com.nhahang.webnhahang.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {

    List<ChiTietHoaDon> findByHoaDon(HoaDon hoaDon);
}