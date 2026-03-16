package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    // Thêm sẵn hàm tìm nhân viên theo tài khoản để sau này làm chức năng Đăng nhập
    NhanVien findByTaiKhoan(String taiKhoan);
}