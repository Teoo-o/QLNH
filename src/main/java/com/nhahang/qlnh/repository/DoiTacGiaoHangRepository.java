package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.DoiTacGiaoHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiTacGiaoHangRepository extends JpaRepository<DoiTacGiaoHang, String> {
}