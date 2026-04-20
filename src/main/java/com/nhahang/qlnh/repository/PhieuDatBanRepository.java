package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.PhieuDatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhieuDatBanRepository extends JpaRepository<PhieuDatBan, String> {
    @Query("SELECT p FROM PhieuDatBan p WHERE p.banAn.maBan = :maBan " +
            "AND p.trangThai <> 'Đã hủy' " +
            "AND p.thoiGian BETWEEN :start AND :end")
    List<PhieuDatBan> findConflictingBookings(@Param("maBan") String maBan,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);
}