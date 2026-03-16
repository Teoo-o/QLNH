package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.DoiTacGiaoHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiTacGiaoHangRepository extends JpaRepository<DoiTacGiaoHang, String> {
}