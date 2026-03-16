package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.PhieuDatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuDatBanRepository extends JpaRepository<PhieuDatBan, String> {
}