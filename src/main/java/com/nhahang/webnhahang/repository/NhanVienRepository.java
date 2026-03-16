package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    //Tự động tìm nhân viên bằng Tài Khoản và Mật Khẩu
    NhanVien findByTaiKhoanAndMatKhau(String taiKhoan, String matKhau);
}