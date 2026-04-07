package com.nhahang.qlnh.controller;

import com.nhahang.qlnh.entity.NhanVien;
import com.nhahang.qlnh.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nhanvien")
@CrossOrigin(origins = "*")
public class NhanVienController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    // 1. Đăng nhập (Chỉ cho phép nhân viên chưa bị xóa đăng nhập)
    @PostMapping("/login")
    public ResponseEntity<?> dangNhap(@RequestBody Map<String, String> loginData) {
        String taiKhoan = loginData.get("taiKhoan");
        String matKhau = loginData.get("matKhau");
        NhanVien nv = nhanVienRepository.findByTaiKhoanAndMatKhau(taiKhoan, matKhau);

        if (nv != null && (nv.getDaXoa() == null || !nv.getDaXoa())) {
            return ResponseEntity.ok(nv);
        }
        return ResponseEntity.status(401).body("Sai tài khoản, mật khẩu hoặc tài khoản đã bị vô hiệu hóa!");
    }

    // 2. Lấy danh sách nhân viên đang hoạt động
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllActive() {
        List<Map<String, Object>> dsAnToan = nhanVienRepository.findByDaXoaFalse().stream()
                .map(nv -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("maNV", nv.getMaNV());
                    map.put("tenNV", nv.getTenNV());
                    map.put("chucVu", nv.getChucVu());
                    map.put("taiKhoan", nv.getTaiKhoan());
                    return map;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(dsAnToan);
    }

    // 3. Lấy danh sách nhân viên trong Thùng Rác
    @GetMapping("/trash")
    public ResponseEntity<List<Map<String, Object>>> getTrash() {
        List<Map<String, Object>> dsThungRac = nhanVienRepository.findByDaXoaTrue().stream()
                .map(nv -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("maNV", nv.getMaNV());
                    map.put("tenNV", nv.getTenNV());
                    map.put("chucVu", nv.getChucVu());
                    map.put("taiKhoan", nv.getTaiKhoan());
                    return map;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(dsThungRac);
    }

    // 4. THÊM NHÂN VIÊN MỚI (BẢN PRO: Xử lý tái chế mã từ Thùng rác)
    @PostMapping
    public ResponseEntity<?> add(@RequestBody NhanVien nvMoi) {
        Optional<NhanVien> nvCu = nhanVienRepository.findById(nvMoi.getMaNV());

        // KỊCH BẢN 1: Mã NV đã tồn tại trong hệ thống
        if (nvCu.isPresent()) {
            NhanVien tonTai = nvCu.get();

            if (!tonTai.getDaXoa()) {
                // Đang hoạt động -> Chặn lại
                return ResponseEntity.badRequest().body("Mã nhân viên này đang có người sử dụng!");
            } else {
                // Đang trong thùng rác -> Tái chế (Ghi đè thông tin người mới vào xác người cũ)

                // Phải kiểm tra xem cái Tên Đăng Nhập mới nhập vào có bị trùng với ai khác không
                if (!tonTai.getTaiKhoan().equals(nvMoi.getTaiKhoan()) && nhanVienRepository.existsByTaiKhoan(nvMoi.getTaiKhoan())) {
                    return ResponseEntity.badRequest().body("Tên tài khoản này đã có người sử dụng!");
                }

                tonTai.setTenNV(nvMoi.getTenNV());
                tonTai.setChucVu(nvMoi.getChucVu());
                tonTai.setTaiKhoan(nvMoi.getTaiKhoan());
                tonTai.setMatKhau(nvMoi.getMatKhau());
                tonTai.setDaXoa(false); // Hồi sinh

                nhanVienRepository.save(tonTai);
                return ResponseEntity.ok("Mã NV này từng nằm trong thùng rác. Hệ thống đã tái sử dụng và thêm người mới thành công!");
            }
        }

        // KỊCH BẢN 2: Mã NV hoàn toàn mới
        if (nhanVienRepository.existsByTaiKhoan(nvMoi.getTaiKhoan())) {
            return ResponseEntity.badRequest().body("Tên tài khoản này đã có người sử dụng!");
        }

        nvMoi.setDaXoa(false);
        nhanVienRepository.save(nvMoi);
        return ResponseEntity.ok("Thêm nhân viên mới thành công!");
    }

    // 5. Sửa thông tin
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody NhanVien nvMoi) {
        return nhanVienRepository.findById(id).map(nv -> {
            nv.setTenNV(nvMoi.getTenNV());
            nv.setChucVu(nvMoi.getChucVu());

            // Bắt lỗi đổi sang tên tài khoản của người khác
            if (!nv.getTaiKhoan().equals(nvMoi.getTaiKhoan()) && nhanVienRepository.existsByTaiKhoan(nvMoi.getTaiKhoan())) {
                throw new RuntimeException("Tên đăng nhập này đã bị trùng với người khác!");
            }
            nv.setTaiKhoan(nvMoi.getTaiKhoan());

            if (nvMoi.getMatKhau() != null && !nvMoi.getMatKhau().trim().isEmpty()) {
                nv.setMatKhau(nvMoi.getMatKhau());
            }
            nhanVienRepository.save(nv);
            return ResponseEntity.ok("Cập nhật thành công!");
        }).orElse(ResponseEntity.badRequest().body("Không tìm thấy nhân viên cần sửa!"));
    }

    // 6. Xóa mềm (Đưa vào Thùng Rác)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDelete(@PathVariable String id) {
        return nhanVienRepository.findById(id).map(nv -> {
            nv.setDaXoa(true);
            nhanVienRepository.save(nv);
            return ResponseEntity.ok("Đã đưa nhân viên vào Thùng Rác!");
        }).orElse(ResponseEntity.badRequest().body("Không tìm thấy nhân viên!"));
    }

    // 7. Khôi phục từ Thùng Rác
    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable String id) {
        return nhanVienRepository.findById(id).map(nv -> {
            nv.setDaXoa(false);
            nhanVienRepository.save(nv);
            return ResponseEntity.ok("Đã khôi phục nhân viên thành công!");
        }).orElse(ResponseEntity.badRequest().body("Không tìm thấy nhân viên!"));
    }
}