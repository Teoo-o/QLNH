package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.HdMa;
import com.nhahang.webnhahang.entity.HdMaId;
import com.nhahang.webnhahang.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Bổ sung import List

@Repository
public interface HdMaRepository extends JpaRepository<HdMa, HdMaId> {

    // Tìm toàn bộ danh sách món ăn thuộc về 1 tờ hóa đơn cụ thể
    List<HdMa> findByHoaDon(HoaDon hoaDon);

}