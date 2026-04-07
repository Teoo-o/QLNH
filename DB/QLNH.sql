CREATE DATABASE QLNH;
GO

USE QLNH;
GO

-- =======================================================
-- TẠO CẤU TRÚC BẢNG
-- =======================================================
CREATE TABLE DANH_MUC (
    MaDM VARCHAR(10) PRIMARY KEY,
    TenDM NVARCHAR(100)
);

CREATE TABLE MON_AN (
    MaMon VARCHAR(10) PRIMARY KEY,
    TenMon NVARCHAR(100),
    DongGia FLOAT,
    HinhAnh VARCHAR(500),
    MaDM VARCHAR(10) FOREIGN KEY REFERENCES DANH_MUC(MaDM),
    DaXoa BIT DEFAULT 0
);

CREATE TABLE BAN_AN (
    MaBan VARCHAR(10) PRIMARY KEY,
    KhuVuc NVARCHAR(50),
    SoGhe INT,
    Status NVARCHAR(50),
    DaXoa BIT DEFAULT 0
);

CREATE TABLE NHAN_VIEN (
    MaNV VARCHAR(10) PRIMARY KEY,
    TenNV NVARCHAR(100),
    ChucVu NVARCHAR(50),
    TaiKhoan VARCHAR(50),
    MatKhau VARCHAR(50),
    DaXoa BIT DEFAULT 0
);

CREATE TABLE KHACH_HANG (
    MaKH VARCHAR(10) PRIMARY KEY,
    TenKH NVARCHAR(100),
    SDT VARCHAR(15),
    DaXoa BIT DEFAULT 0
);

CREATE TABLE PHIEU_DAT_BAN (
    MaPhieu VARCHAR(10) PRIMARY KEY,
    ThoiGian DATETIME,
    TrangThai NVARCHAR(50),
    SoNguoi INT,
    MaKH VARCHAR(10) FOREIGN KEY REFERENCES KHACH_HANG(MaKH),
    MaBan VARCHAR(10) FOREIGN KEY REFERENCES BAN_AN(MaBan)
);

CREATE TABLE NGUYEN_LIEU (
    MaNL VARCHAR(10) PRIMARY KEY,
    TenNL NVARCHAR(100),
    DonViTinh NVARCHAR(20),
    SL FLOAT,
    DaXoa BIT DEFAULT 0
);

CREATE TABLE MA_NL (
    MaMon VARCHAR(10) FOREIGN KEY REFERENCES MON_AN(MaMon),
    MaNL VARCHAR(10) FOREIGN KEY REFERENCES NGUYEN_LIEU(MaNL),
    DinhLuong FLOAT,
    PRIMARY KEY (MaMon, MaNL)
);

CREATE TABLE KHUYEN_MAI (
    MaKM VARCHAR(10) PRIMARY KEY,
    TenKM NVARCHAR(100),
    NgayBD DATETIME,
    PhanTramGiam FLOAT,
    NgayKT DATETIME,
    DaXoa BIT DEFAULT 0
);

CREATE TABLE DOI_TAC_GIAO_HANG (
    MaDT VARCHAR(10) PRIMARY KEY,
    TenDT NVARCHAR(100),
    PhanTramNenTang FLOAT,
    DaXoa BIT DEFAULT 0
);

CREATE TABLE HOA_DON (
    MaHD VARCHAR(20) PRIMARY KEY,
    LoaiHD NVARCHAR(50),
    PhiGiaoHang FLOAT,
    SoTienGiam FLOAT,
    PhiNenTang FLOAT,
    NgayTao DATETIME,
    PTTT NVARCHAR(50),
    MaKM VARCHAR(10) FOREIGN KEY REFERENCES KHUYEN_MAI(MaKM),
    MaPhieu VARCHAR(10) FOREIGN KEY REFERENCES PHIEU_DAT_BAN(MaPhieu),
    MaNV VARCHAR(10) FOREIGN KEY REFERENCES NHAN_VIEN(MaNV),
    MaDT VARCHAR(10) FOREIGN KEY REFERENCES DOI_TAC_GIAO_HANG(MaDT),
    MaBan VARCHAR(10) FOREIGN KEY REFERENCES BAN_AN(MaBan),
    MaKH VARCHAR(10) FOREIGN KEY REFERENCES KHACH_HANG(MaKH)
);

CREATE TABLE HD_MA (
    MaHD VARCHAR(20) FOREIGN KEY REFERENCES HOA_DON(MaHD),
    MaMon VARCHAR(10) FOREIGN KEY REFERENCES MON_AN(MaMon),
    SL INT,
    DonGiaBan FLOAT,
    PRIMARY KEY (MaHD, MaMon)
);
GO

-- =======================================================
-- DỮ LIỆU MẪU
-- =======================================================

INSERT INTO NHAN_VIEN (MaNV, TenNV, ChucVu, TaiKhoan, MatKhau) VALUES
('NV01', N'Trần Đại Quản', N'Quản lý', 'admin', '123456'),
('NV02', N'Nguyễn Thu Ngân', N'Thu ngân', 'thungan1', '123456'),
('NV03', N'Lê Phục Vụ', N'Phục vụ', 'phucvu1', '123456'),
('NV04', N'Phạm Bếp Trưởng', N'Đầu bếp', 'bep1', '123456'),
('NV05', N'Hoàng Giao Hàng', N'Phục vụ', 'phucvu2', '123456');

INSERT INTO BAN_AN (MaBan, KhuVuc, SoGhe, Status) VALUES
('B01', N'Tầng 1 - Sảnh', 4, N'Trống'),
('B02', N'Tầng 1 - Sảnh', 4, N'Có khách'),
('B03', N'Tầng 1 - Sảnh', 6, N'Trống'),
('B04', N'Tầng 1 - Cửa sổ', 2, N'Đã đặt'),
('B05', N'Tầng 1 - Cửa sổ', 2, N'Trống'),
('B06', N'Tầng 2 - VIP', 8, N'Trống'),
('B07', N'Tầng 2 - VIP', 8, N'Có khách'),
('B08', N'Tầng 2 - Ban công', 4, N'Trống'),
('B09', N'Tầng 2 - Ban công', 4, N'Đã đặt'),
('B10', N'Tầng 2 - Ban công', 4, N'Trống');

INSERT INTO DANH_MUC (MaDM, TenDM) VALUES
('DM01', N'Món Nước'),
('DM02', N'Món Cơm'),
('DM03', N'Đồ Ăn Vặt'),
('DM04', N'Đồ Uống');

INSERT INTO MON_AN (MaMon, TenMon, DongGia, HinhAnh, MaDM) VALUES
('M01', N'Phở Bò Tái Nạm', 45000, '', 'DM01'),
('M02', N'Phở Gà Ta', 40000, '', 'DM01'),
('M03', N'Bún Chả Hà Nội', 50000, '', 'DM01'),
('M04', N'Bún Bò Huế', 55000, '', 'DM01'),
('M05', N'Cơm Rang Dưa Bò', 45000, '', 'DM02'),
('M06', N'Cơm Sườn Nướng', 50000, '', 'DM02'),
('M07', N'Cơm Gà Xối Mỡ', 45000, '', 'DM02'),
('M08', N'Khoai Tây Chiên', 30000, '', 'DM03'),
('M09', N'Nem Chua Rán', 40000, '', 'DM03'),
('M10', N'Cánh Gà Chiên Mắm', 60000, '', 'DM03'),
('M11', N'Trà Đá', 5000, '', 'DM04'),
('M12', N'Trà Chanh', 15000, '', 'DM04'),
('M13', N'Nước Ép Cam', 35000, '', 'DM04'),
('M14', N'Trà Sữa Trân Châu', 40000, '', 'DM04'),
('M15', N'Bia Tiger', 25000, '', 'DM04');

INSERT INTO NGUYEN_LIEU (MaNL, TenNL, DonViTinh, SL) VALUES
('NL01', N'Thịt Bò', 'Kg', 15.5),
('NL02', N'Thịt Gà', 'Kg', 20.0),
('NL03', N'Bánh Phở', 'Kg', 30.0),
('NL04', N'Bún Tươi', 'Kg', 25.0),
('NL05', N'Gạo Tẻ', 'Kg', 50.0),
('NL06', N'Khoai Tây', 'Kg', 10.0),
('NL07', N'Cánh Gà', 'Kg', 12.0),
('NL08', N'Trà Đen', 'Kg', 5.0),
('NL09', N'Cam Tươi', 'Kg', 8.0),
('NL10', N'Trân Châu', 'Kg', 10.0);

INSERT INTO MA_NL (MaMon, MaNL, DinhLuong) VALUES
('M01', 'NL01', 0.15),
('M01', 'NL03', 0.2), 
('M02', 'NL02', 0.15),
('M02', 'NL03', 0.2), 
('M05', 'NL01', 0.1), 
('M05', 'NL05', 0.2), 
('M08', 'NL06', 0.3), 
('M10', 'NL07', 0.4), 
('M12', 'NL08', 0.05),
('M13', 'NL09', 0.5), 
('M14', 'NL08', 0.05),
('M14', 'NL10', 0.1); 

INSERT INTO KHACH_HANG (MaKH, TenKH, SDT) VALUES
('KH01', N'Nguyễn Văn A', '0901234567'),
('KH02', N'Trần Thị B', '0912345678'),
('KH03', N'Lê Minh C', '0987654321');

INSERT INTO DOI_TAC_GIAO_HANG (MaDT, TenDT, PhanTramNenTang) VALUES
('DT01', N'ShopeeFood', 20.0),
('DT02', N'GrabFood', 25.0),
('DT03', N'BeFood', 15.0);

INSERT INTO KHUYEN_MAI (MaKM, TenKM, NgayBD, PhanTramGiam, NgayKT) VALUES
('KM01', N'Giảm giá khai trương', '2026-03-01', 10.0, '2026-04-01'),
('KM02', N'Lễ Tình Nhân', '2026-02-14', 15.0, '2026-02-15');

INSERT INTO PHIEU_DAT_BAN (MaPhieu, ThoiGian, TrangThai, SoNguoi, MaKH, MaBan) VALUES
('PDB01', '2026-04-01 19:00:00', N'Chờ xác nhận', 2, 'KH01', 'B04'),
('PDB02', '2026-04-02 20:00:00', N'Đã nhận bàn', 4, 'KH02', 'B09');

INSERT INTO HOA_DON (MaHD, LoaiHD, PhiGiaoHang, SoTienGiam, PhiNenTang, NgayTao, PTTT, MaNV, MaBan) VALUES
('HD1711900001', N'Tại chỗ', 0, 0, 0, '2026-03-25 12:30:00', N'Tiền mặt', 'NV02', 'B02'),
('HD1711900002', N'Tại chỗ', 0, 0, 0, '2026-03-25 18:45:00', N'Chưa thanh toán', 'NV03', 'B07');

INSERT INTO HD_MA (MaHD, MaMon, SL, DonGiaBan) VALUES
('HD1711900001', 'M01', 2, 45000),
('HD1711900001', 'M11', 2, 5000),
('HD1711900002', 'M05', 1, 45000),
('HD1711900002', 'M10', 1, 60000),
('HD1711900002', 'M15', 3, 25000);
GO