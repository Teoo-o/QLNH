package com.nhahang.qlnh.repository;

import com.nhahang.qlnh.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    // Tìm nhân viên bằng Tài Khoản và Mật Khẩu (Dùng cho Đăng nhập)
    NhanVien findByTaiKhoanAndMatKhau(String taiKhoan, String matKhau);

    // Kiểm tra trùng tài khoản
    boolean existsByTaiKhoan(String taiKhoan);

    // Lấy danh sách nhân viên đang làm việc (Chưa bị xóa)
    List<NhanVien> findByDaXoaFalse();

    // Lấy danh sách nhân viên trong thùng rác (Đã bị xóa)
    List<NhanVien> findByDaXoaTrue();
}