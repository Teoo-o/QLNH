package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, String> {
}