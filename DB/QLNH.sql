CREATE DATABASE QLNH;
USE QLNH;


CREATE TABLE KHACH_HANG (
    MaKH VARCHAR(20) PRIMARY KEY,
    TenKH NVARCHAR(100) NOT NULL,
    SDT VARCHAR(15)
);

CREATE TABLE BAN_AN (
    MaBan VARCHAR(20) PRIMARY KEY,
    KhuVuc NVARCHAR(50), 
    SoGhe INT,
    Status NVARCHAR(50) 
);

CREATE TABLE DOI_TAC_GIAO_HANG (
    MaDT VARCHAR(20) PRIMARY KEY,
    TenDT NVARCHAR(100) NOT NULL,
    PhanTramNenTang FLOAT 
);

CREATE TABLE NHAN_VIEN (
    MaNV VARCHAR(20) PRIMARY KEY,
    TenNV NVARCHAR(100) NOT NULL, 
    ChucVu NVARCHAR(50),         
    TaiKhoan VARCHAR(50) UNIQUE,
    MatKhau VARCHAR(255) 
);

CREATE TABLE DANH_MUC (
    MaDM VARCHAR(20) PRIMARY KEY,
    TenDM NVARCHAR(100) NOT NULL  
);

CREATE TABLE KHUYEN_MAI (
    MaKM VARCHAR(20) PRIMARY KEY,
    TenKM NVARCHAR(255) NOT NULL, 
    NgayBD DATETIME,
    PhanTramGiam FLOAT,
    NgayKT DATETIME
);

CREATE TABLE NGUYEN_LIEU (
    MaNL VARCHAR(20) PRIMARY KEY,
    TenNL NVARCHAR(100) NOT NULL, 
    DonViTinh NVARCHAR(20),      
    SL FLOAT 
);

CREATE TABLE MON_AN (
    MaMon VARCHAR(20) PRIMARY KEY,
    TenMon NVARCHAR(100) NOT NULL, 
    MaDM VARCHAR(20) FOREIGN KEY REFERENCES DANH_MUC(MaDM),
    DongGia DECIMAL(18,2),
    HinhAnh VARCHAR(500) 
);

CREATE TABLE PHIEU_DAT_BAN (
    MaPhieu VARCHAR(20) PRIMARY KEY,
    ThoiGian DATETIME,
    TrangThai NVARCHAR(50),      
    SoNguoi INT,
    MaKH VARCHAR(20) FOREIGN KEY REFERENCES KHACH_HANG(MaKH),
    MaBan VARCHAR(20) FOREIGN KEY REFERENCES BAN_AN(MaBan)
);

CREATE TABLE HOA_DON (
    MaHD VARCHAR(20) PRIMARY KEY,
    LoaiHD NVARCHAR(50),         
    PhiGiaoHang DECIMAL(18,2),
    SoTienGiam DECIMAL(18,2),
    MaKM VARCHAR(20) FOREIGN KEY REFERENCES KHUYEN_MAI(MaKM),
    MaPhieu VARCHAR(20) FOREIGN KEY REFERENCES PHIEU_DAT_BAN(MaPhieu),
    MaNV VARCHAR(20) FOREIGN KEY REFERENCES NHAN_VIEN(MaNV),
    MaDT VARCHAR(20) FOREIGN KEY REFERENCES DOI_TAC_GIAO_HANG(MaDT),
    NgayTao DATETIME DEFAULT GETDATE(),
    MaBan VARCHAR(20) FOREIGN KEY REFERENCES BAN_AN(MaBan),
    PhiNenTang DECIMAL(18,2), 
    MaKH VARCHAR(20) FOREIGN KEY REFERENCES KHACH_HANG(MaKH),
    PTTT NVARCHAR(50)            
);

CREATE TABLE MA_NL (
    MaMon VARCHAR(20) FOREIGN KEY REFERENCES MON_AN(MaMon),
    MaNL VARCHAR(20) FOREIGN KEY REFERENCES NGUYEN_LIEU(MaNL),
    DinhLuong FLOAT,
    PRIMARY KEY (MaMon, MaNL) 
);

CREATE TABLE HD_MA (
    MaHD VARCHAR(20) FOREIGN KEY REFERENCES HOA_DON(MaHD),
    MaMon VARCHAR(20) FOREIGN KEY REFERENCES MON_AN(MaMon),
    SL INT,
    DonGiaBan DECIMAL(18,2),
    PRIMARY KEY (MaHD, MaMon) 
);


-- 1. Khách hàng
INSERT INTO KHACH_HANG (MaKH, TenKH, SDT) VALUES 
('KH01', N'Trung', '0901234567'),
('KH02', N'Hoàng', '0987654321'),
('KH03', N'Khách vãng lai', NULL);

-- 2. Bàn ăn
INSERT INTO BAN_AN (MaBan, KhuVuc, SoGhe, Status) VALUES 
('B01', N'Tầng 1', 4, N'Trống'),
('B02', N'Tầng 1', 4, N'Có khách'),
('B03', N'Phòng VIP', 10, N'Đã đặt');

-- 3. Đối tác giao hàng
INSERT INTO DOI_TAC_GIAO_HANG (MaDT, TenDT, PhanTramNenTang) VALUES 
('DT01', N'GrabFood', 20.0),
('DT02', N'ShopeeFood', 25.0);

-- 4. Nhân viên
INSERT INTO NHAN_VIEN (MaNV, TenNV, ChucVu, TaiKhoan, MatKhau) VALUES 
('NV01', N'Nguyễn Văn Tiến', N'Quản lý', 'admin', '123456'),
('NV02', N'Trần Thị Anh', N'Thu ngân', 'thungan', '123456');

-- 5. Danh mục
INSERT INTO DANH_MUC (MaDM, TenDM) VALUES 
('DM01', N'Món chính'),
('DM02', N'Đồ uống giải khát');

-- 6. Khuyến mãi
INSERT INTO KHUYEN_MAI (MaKM, TenKM, NgayBD, PhanTramGiam, NgayKT) VALUES 
('KM01', N'Khai trương giảm giá 10%', '2026-01-01', 10.0, '2026-12-31'),
('KM02', N'Khuyến mãi ngày Lễ', '2026-03-01', 20.0, '2026-03-31');

-- 7. Nguyên liệu (Kho)
INSERT INTO NGUYEN_LIEU (MaNL, TenNL, DonViTinh, SL) VALUES 
('NL01', N'Thịt bò tươi', N'Kg', 15.5),
('NL02', N'Bánh phở tươi', N'Kg', 20.0),
('NL03', N'Trà lài', N'Hộp', 50.0);

-- 8. Món ăn
INSERT INTO MON_AN (MaMon, TenMon, MaDM, DongGia, HinhAnh) VALUES 
('M01', N'Phở bò đặc biệt', 'DM01', 50000.00, 'pho_bo.jpg'),
('M02', N'Trà đá', 'DM02', 5000.00, 'tra_da.jpg');

-- 9. Định lượng nguyên liệu (1 Phở bò cần 0.15kg Bò + 0.2kg Phở)
INSERT INTO MA_NL (MaMon, MaNL, DinhLuong) VALUES 
('M01', 'NL01', 0.15),
('M01', 'NL02', 0.20);

-- 10. Phiếu đặt bàn
INSERT INTO PHIEU_DAT_BAN (MaPhieu, ThoiGian, TrangThai, SoNguoi, MaKH, MaBan) VALUES 
('P01', '2026-03-20 19:00:00', N'Chờ xác nhận', 8, 'KH01', 'B03');

-- 11. Hóa đơn
INSERT INTO HOA_DON (MaHD, LoaiHD, PhiGiaoHang, SoTienGiam, MaKM, MaPhieu, MaNV, MaDT, MaBan, PhiNenTang, MaKH, PTTT) VALUES 
('HD01', N'Tại chỗ', 0, 0, NULL, NULL, 'NV02', NULL, 'B02', 0, 'KH02', N'Chưa thanh toán'),
('HD02', N'Giao hàng', 15000, 5000, 'KM01', NULL, 'NV02', 'DT01', NULL, 10000, 'KH03', N'Chuyển khoản');

-- 12. Chi tiết hóa đơn
INSERT INTO HD_MA (MaHD, MaMon, SL, DonGiaBan) VALUES 
('HD01', 'M01', 2, 50000.00), 
('HD01', 'M02', 2, 5000.00),  
('HD02', 'M01', 1, 50000.00);