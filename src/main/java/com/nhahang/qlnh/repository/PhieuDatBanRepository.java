package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.PhieuDatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuDatBanRepository extends JpaRepository<PhieuDatBan, String> {
}