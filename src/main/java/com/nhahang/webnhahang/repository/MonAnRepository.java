package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, String> {
}