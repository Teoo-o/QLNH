package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.HdMa;
import com.nhahang.qlnh.entity.HdMaKey; // DÒNG QUAN TRỌNG ĐỂ JAVA NHẬN DIỆN KHÓA KÉP
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HdMaRepository extends JpaRepository<HdMa, HdMaKey> {

    // Tìm toàn bộ danh sách các món ăn đã gọi thuộc về 1 mã hóa đơn
    List<HdMa> findByHoaDon_MaHD(String maHD);
}