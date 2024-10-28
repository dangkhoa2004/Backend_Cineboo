-- Tạo cơ sở dữ liệu và chọn nó
CREATE DATABASE IF NOT EXISTS DatVeXemPhimCineBoo;
USE DatVeXemPhimCineBoo;

-- Bảng PhanLoaiChucVu
CREATE TABLE PhanLoaiChucVu (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaChucVu VARCHAR(15) NOT NULL,
    TenChucVu VARCHAR(150) NOT NULL,
    TrangThai INT
);

-- Bảng PhanLoaiKhachHang
CREATE TABLE PhanLoaiKhachHang (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaKhachHang VARCHAR(15) NOT NULL,
    LoaiKhachHang VARCHAR(100) NOT NULL,
    TrangThai INT
);

-- Bảng Voucher
CREATE TABLE Voucher (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaVoucher VARCHAR(15) NOT NULL,
    GiaTriDoi INT NOT NULL,
    TruTienPhanTram INT,
    TruTienSo DECIMAL(18,2),
    SoTienGiam DECIMAL(18,2),
    SoTienToiThieu DECIMAL(18,2),
    GiamToiDa DECIMAL(18,2),
    NgayBatDau DATE,
    NgayKetThuc DATE,
    TrangThai INT
);

-- Bảng Phim
CREATE TABLE Phim (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaPhim VARCHAR(15) NOT NULL,
    TenPhim VARCHAR(255) NOT NULL,
    AnhPhim VARCHAR(255) NOT NULL,
    DienVien VARCHAR(255) NOT NULL,
    TheLoai VARCHAR(200) NOT NULL,
    Nam INT NOT NULL,
    NoiDungMoTa TEXT NULL,
    Trailer VARCHAR(255) NULL,
    ThoiLuong INT NOT NULL,
    QuocGia VARCHAR(100) NOT NULL,
    NoiDung TEXT NULL,
    GioiHanDoTuoi INT NOT NULL,
    TrangThai INT
);

-- Bảng PhongChieu
CREATE TABLE PhongChieu (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaPhong VARCHAR(15) NOT NULL,
    TongSoGhe INT NOT NULL,
    TrangThai INT
);

-- Bảng Ghe
CREATE TABLE Ghe (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaGhe VARCHAR(15) NOT NULL,
    GiaTien DECIMAL(18,2) NOT NULL,
    ID_PhongChieu INT,
    TrangThai INT,
    FOREIGN KEY (ID_PhongChieu) REFERENCES PhongChieu(ID)
);

-- Bảng SuatChieu
CREATE TABLE SuatChieu (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaSuatChieu VARCHAR(15) NOT NULL,
    ThoiGianChieu DATETIME NOT NULL,
    ID_Phim INT,
    ID_PhongChieu INT,
    TrangThai INT,
    FOREIGN KEY (ID_Phim) REFERENCES Phim(ID),
    FOREIGN KEY (ID_PhongChieu) REFERENCES PhongChieu(ID)
);

-- Bảng TheLoaiPhim
CREATE TABLE TheLoaiPhim (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaTLPhim VARCHAR(15) NOT NULL,
    TenTheLoai VARCHAR(100) NOT NULL,
    TrangThai INT
);

-- Bảng DanhSachTLPhim
CREATE TABLE DanhSachTLPhim (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_Phim INT,
    ID_TLPhim INT,
    TrangThai INT,
    FOREIGN KEY (ID_Phim) REFERENCES Phim(ID),
    FOREIGN KEY (ID_TLPhim) REFERENCES TheLoaiPhim(ID)
);

-- Bảng PTTT
CREATE TABLE PTTT (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaPTTT VARCHAR(15) NOT NULL,
    TenPTTT VARCHAR(200) NOT NULL,
    TrangThai INT
);

-- Bảng NhanVien
CREATE TABLE NhanVien (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_ChucVu INT,
    MaNhanVien VARCHAR(15) NOT NULL,
    Ten VARCHAR(100) NOT NULL,
    TenDem VARCHAR(100) NOT NULL,
    Ho VARCHAR(50) NOT NULL,
    NgaySinh DATE NOT NULL,
    GioiTinh INT NOT NULL,
    Email VARCHAR(100) NOT NULL,
    DanToc VARCHAR(50) NULL,
    DiaChi VARCHAR(255) NOT NULL,
    TrangThai INT,
    FOREIGN KEY (ID_ChucVu) REFERENCES PhanLoaiChucVu(ID)
);

-- Bảng KhachHang
CREATE TABLE KhachHang (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_PhanLoai INT,
    Ten VARCHAR(15) NOT NULL,
    TenDem VARCHAR(50) NULL,
    Ho VARCHAR(50) NOT NULL,
    NgaySinh DATE NOT NULL,
    SoDienThoai VARCHAR(10) NOT NULL,
    GioiTinh INT NOT NULL,
    Email VARCHAR(255) NOT NULL,
    DanToc VARCHAR(75) NULL,
    DiaChi VARCHAR(255) NOT NULL,
    Diem INT,
    TrangThai INT,
    FOREIGN KEY (ID_PhanLoai) REFERENCES PhanLoaiKhachHang(ID)
);

-- Bảng KhoQua
CREATE TABLE KhoQua (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_KhachHang INT,
    ID_Voucher INT,
    FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID),
    FOREIGN KEY (ID_Voucher) REFERENCES Voucher(ID)
);

-- Bảng HoaDon
CREATE TABLE HoaDon (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_KhachHang INT,
    ID_Phim INT,
    ID_Voucher INT,
    ID_PTTT INT,
    MaHoaDon VARCHAR(15) NOT NULL,
    SoLuong INT NOT NULL,
    ThoiGianThanhToan DATETIME,
    Diem INT NOT NULL,
    TongSoTien DECIMAL(18,2) NOT NULL,
    TrangThai INT,
    FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID),
    FOREIGN KEY (ID_Phim) REFERENCES Phim(ID),
    FOREIGN KEY (ID_Voucher) REFERENCES Voucher(ID),
    FOREIGN KEY (ID_PTTT) REFERENCES PTTT(ID)
);

-- Bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_HoaDon INT,
    ID_Ghe INT,
    TrangThai INT,
    FOREIGN KEY (ID_HoaDon) REFERENCES HoaDon(ID),
    FOREIGN KEY (ID_Ghe) REFERENCES Ghe(ID)
);
INSERT INTO PhanLoaiChucVu (MaChucVu, TenChucVu, TrangThai)
VALUES
    ('CV001', 'Quản lý', 1),
    ('CV002', 'Nhân viên bán vé', 1),
    ('CV003', 'Nhân viên kỹ thuật', 1),
    ('CV004', 'Nhân viên chăm sóc khách hàng', 1),
    ('CV005', 'Quản trị viên', 1),
    ('CV006', 'Nhân viên vệ sinh', 1),
    ('CV007', 'Bảo vệ', 1),
    ('CV008', 'Nhân viên thu ngân', 1),
    ('CV009', 'Nhân viên kế toán', 1),
    ('CV010', 'Nhân viên Marketing', 1);
INSERT INTO PhanLoaiKhachHang (MaKhachHang, LoaiKhachHang, TrangThai)
VALUES
    ('KH001', 'Thành viên thường', 1),
    ('KH002', 'Thành viên VIP', 1),
    ('KH003', 'Thành viên Platinum', 1),
    ('KH004', 'Thành viên Vàng', 1),
    ('KH005', 'Thành viên Bạc', 1),
    ('KH006', 'Thành viên Đồng', 1),
    ('KH007', 'Thành viên Sinh viên', 1),
    ('KH008', 'Thành viên Gia đình', 1),
    ('KH009', 'Thành viên Doanh nghiệp', 1),
    ('KH010', 'Thành viên Khách mời', 1);
INSERT INTO Voucher (MaVoucher, GiaTriDoi, TruTienPhanTram, TruTienSo, SoTienGiam, SoTienToiThieu, GiamToiDa, NgayBatDau, NgayKetThuc, TrangThai)
VALUES
    ('VC001', 500, 10, NULL, 50000, 100000, 50000, '2024-01-01', '2024-12-31', 1),
    ('VC002', 1000, NULL, 20000, 0, 50000, 20000, '2024-01-01', '2024-06-30', 1),
    ('VC003', 700, 5, NULL, 35000, 70000, 30000, '2024-01-01', '2024-06-30', 1),
    ('VC004', 800, NULL, 30000, 0, 80000, 30000, '2024-01-01', '2024-12-31', 1),
    ('VC005', 1500, 20, NULL, 75000, 120000, 50000, '2024-01-01', '2024-12-31', 1),
    ('VC006', 600, NULL, 25000, 0, 60000, 20000, '2024-01-01', '2024-09-30', 1),
    ('VC007', 100, NULL, NULL, 15000, 50000, 15000, '2024-01-01', '2024-04-30', 1),
    ('VC008', 200, 8, NULL, 25000, 70000, 30000, '2024-01-01', '2024-05-31', 1),
    ('VC009', 300, NULL, 40000, 0, 80000, 35000, '2024-01-01', '2024-08-31', 1),
    ('VC010', 900, 15, NULL, 45000, 90000, 50000, '2024-01-01', '2024-12-31', 1);
INSERT INTO Phim (MaPhim, TenPhim, AnhPhim, DienVien, TheLoai, Nam, NoiDungMoTa, Trailer, ThoiLuong, QuocGia, GioiHanDoTuoi, TrangThai)
VALUES
    ('P001', 'Biệt Đội Siêu Anh Hùng', 'https://anhphim.com/1.jpg', 'Chris Evans, Robert Downey Jr.', 'Hành động, Phiêu lưu', 2019, 'Phim hành động hấp dẫn', 'https://trailer.com/1', 120, 'Mỹ', 1, 1),
    ('P002', 'Sát Thủ John Wick', 'https://anhphim.com/2.jpg', 'Keanu Reeves', 'Hành động, Hình sự', 2014, 'Phim sát thủ nổi tiếng', 'https://trailer.com/2', 130, 'Mỹ', 2, 1),
    -- Thêm các phim khác tương tự
    ('P010', 'Chúa Tể Những Chiếc Nhẫn', 'https://anhphim.com/10.jpg', 'Elijah Wood, Viggo Mortensen', 'Phiêu lưu, Kỳ ảo', 2001, 'Hành trình tiêu diệt chiếc nhẫn', 'https://trailer.com/10', 178, 'New Zealand', 1, 1);
INSERT INTO PhongChieu (MaPhong, TongSoGhe, TrangThai)
VALUES
    ('PC01', 100, 1),
    ('PC02', 150, 1),
    ('PC10', 75, 1);
INSERT INTO Ghe (MaGhe, GiaTien, ID_PhongChieu, TrangThai)
VALUES
    ('S1', 70000, 1, 1),
    ('S2', 70000, 1, 1),
    ('S3', 70000, 2, 1);
INSERT INTO SuatChieu (MaSuatChieu, ThoiGianChieu, ID_Phim, ID_PhongChieu, TrangThai)
VALUES
    ('SC001', '2024-11-01 14:30:00', 1, 1, 1),
    ('SC002', '2024-11-01 17:30:00', 2, 1, 1),
    -- Thêm các suất chiếu khác tương tự
    ('SC010', '2024-11-01 20:00:00', 3, 2, 1);
INSERT INTO TheLoaiPhim (MaTLPhim, TenTheLoai, TrangThai)
VALUES
    ('TL01', 'Hành động', 1),
    ('TL02', 'Kinh dị', 1),
    -- Thêm các thể loại phim khác tương tự
    ('TL10', 'Hoạt hình', 1);
INSERT INTO DanhSachTLPhim (ID_Phim, ID_TLPhim, TrangThai)
VALUES
    (1, 1, 1),
    (2, 2, 1),
    -- Thêm các cặp phim - thể loại khác tương tự
    (3, 3, 1);
INSERT INTO PTTT (MaPTTT, TenPTTT, TrangThai)
VALUES
    ('PT01', 'Thanh toán tiền mặt', 1),
    ('PT02', 'Thanh toán thẻ', 1),
    -- Thêm các phương thức thanh toán khác tương tự
    ('PT10', 'Chuyển khoản ngân hàng', 1);
INSERT INTO KhachHang (ID_PhanLoai, Ten, TenDem, Ho, NgaySinh, SoDienThoai, GioiTinh, Email, DanToc, DiaChi, Diem, TrangThai)
VALUES
    (1, 'Hoàng', 'Đức', 'Nguyễn', '1995-05-15', '0987654321', 1, 'hoang.nguyen@example.com', 'Kinh', '789 XYZ, Hà Nội', 200, 1),
    -- Thêm các khách hàng khác tương tự
    (2, 'Mai', 'Thị', 'Phạm', '1988-08-08', '0912345678', 0, 'mai.pham@example.com', 'Kinh', '987 UVW, Hà Nội', 300, 1);



CREATE TABLE DoTuoi(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    MaDoTuoi VARCHAR(25),
    TenDoTuoi VARCHAR(5),
);

INSERT INTO DoTuoi VALUES (1,"DT001","R15"),
INSERT INTO DoTuoi VALUES (2,"DT002","R18"),
INSERT INTO DoTuoi VALUES (3, "DT002","R13"),