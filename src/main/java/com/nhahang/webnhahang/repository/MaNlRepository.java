package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.MaNl;
import com.nhahang.webnhahang.entity.MaNlId;
import com.nhahang.webnhahang.entity.MonAn; // Thêm import MonAn
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaNlRepository extends JpaRepository<MaNl, MaNlId> {

    // Tìm định lượng nguyên liệu (công thức) của một món ăn cụ thể
    List<MaNl> findByMonAn(MonAn monAn);
}