package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {

    // Tìm các hóa đơn khách đang ăn (chưa thanh toán) để thu ngân biết đường tính tiền
    @Query("SELECT h FROM HoaDon h WHERE h.pttt = 'Chưa thanh toán'")
    List<HoaDon> timHoaDonChuaThanhToan();
}