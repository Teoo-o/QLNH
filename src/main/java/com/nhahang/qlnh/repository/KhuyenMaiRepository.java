package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, String> {

    // Câu lệnh "trấn yểm": Tìm mã KM và kiểm tra xem thời điểm hiện tại có nằm trong hạn sử dụng không
    @Query("SELECT k FROM KhuyenMai k WHERE k.maKM = :maKM AND :thoiGianHienTai BETWEEN k.ngayBD AND k.ngayKT")
    Optional<KhuyenMai> timMaHopLe(@Param("maKM") String maKM, @Param("thoiGianHienTai") LocalDateTime thoiGianHienTai);
}