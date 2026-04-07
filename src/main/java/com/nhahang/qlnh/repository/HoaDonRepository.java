package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {

    // Tìm hóa đơn của một bàn cụ thể đang ở trạng thái chưa thanh toán
    List<HoaDon> findByBanAn_MaBanAndPttt(String maBan, String pttt);
}