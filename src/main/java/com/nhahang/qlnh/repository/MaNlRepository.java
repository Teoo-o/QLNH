package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.MaNl;
import com.nhahang.qlnh.entity.MaNlKey;
import com.nhahang.qlnh.entity.MonAn; // Import thêm để Java nhận diện
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaNlRepository extends JpaRepository<MaNl, MaNlKey> {

    // Tìm bằng Mã Món (String)
    List<MaNl> findByMonAn_MaMon(String maMon);

    // BỔ SUNG: Tìm bằng nguyên Object Món Ăn (Fix lỗi của bạn)
    List<MaNl> findByMonAn(MonAn monAn);
}