

-- Bảng PhanLoaiChucVu
CREATE TABLE PhanLoaiChucVu (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                MaChucVu VARCHAR(15) NOT NULL,
                                TenChucVu VARCHAR(150) NOT NULL,
                                TrangThaiPhanLoaiChucVu INT
);

-- Bảng PhanLoaiKhachHang
CREATE TABLE PhanLoaiKhachHang (
                                   ID INT AUTO_INCREMENT PRIMARY KEY,
                                   MaKhachHang VARCHAR(15) NOT NULL,
                                   LoaiKhachHang VARCHAR(100) NOT NULL,
                                   TrangThaiPhanLoaiKhachHang INT
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
                         TrangThaiVoucher INT
);

-- Bảng Phim
CREATE TABLE Phim (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      MaPhim VARCHAR(15) NOT NULL,
                      TenPhim VARCHAR(255) NOT NULL,
                      AnhPhim VARCHAR(255) NOT NULL,
                      DienVien VARCHAR(255) NOT NULL,
                      Nam INT NOT NULL,
                      NoiDungMoTa TEXT,
                      Trailer VARCHAR(255),
                      NgayRaMat DATE NOT NULL,
                      ThoiLuong INT NOT NULL,
                      QuocGia VARCHAR(100) NOT NULL,
                      NoiDung TEXT,
                      GioiHanDoTuoi INT NOT NULL,
                      TrangThai INT
);

-- Bảng PhongChieu
CREATE TABLE PhongChieu (
                            ID INT AUTO_INCREMENT PRIMARY KEY,
                            MaPhong VARCHAR(15) NOT NULL,
                            TongSoGhe INT NOT NULL,
                            TrangThaiPhongChieu INT
);

-- Bảng Ghe
CREATE TABLE Ghe (
                     ID INT AUTO_INCREMENT PRIMARY KEY,
                     MaGhe VARCHAR(15) NOT NULL,
                     GiaTien DECIMAL(18,2) NOT NULL,
                     ID_PhongChieu INT,
                     TrangThaiGhe INT,
                     FOREIGN KEY (ID_PhongChieu) REFERENCES PhongChieu(ID) ON DELETE CASCADE
);

-- Bảng SuatChieu
CREATE TABLE SuatChieu (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           MaSuatChieu VARCHAR(15) NOT NULL,
                           ThoiGianChieu DATETIME NOT NULL,
                           ID_Phim INT,
                           ID_PhongChieu INT,
                           TrangThaiSuatChieu INT,
                           FOREIGN KEY (ID_Phim) REFERENCES Phim(ID) ON DELETE CASCADE,
                           FOREIGN KEY (ID_PhongChieu) REFERENCES PhongChieu(ID) ON DELETE CASCADE
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
                                FOREIGN KEY (ID_Phim) REFERENCES Phim(ID) ON DELETE CASCADE,
                                FOREIGN KEY (ID_TLPhim) REFERENCES TheLoaiPhim(ID) ON DELETE CASCADE
);

-- Bảng PTTT
CREATE TABLE PTTT (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      MaPTTT VARCHAR(15) NOT NULL,
                      TenPTTT VARCHAR(200) NOT NULL,
                      TrangThaiPTTT INT
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
                          TaiKhoan VARCHAR(100) NOT NULL,
                          MatKhau VARCHAR(100) NOT NULL,
                          DanToc VARCHAR(50),
                          DiaChi VARCHAR(255) NOT NULL,
                          TrangThaiNhanVien INT,
                          FOREIGN KEY (ID_ChucVu) REFERENCES PhanLoaiChucVu(ID) ON DELETE SET NULL
);

-- Bảng KhachHang
CREATE TABLE KhachHang (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           ID_PhanLoai INT,
                           MaKhachHang VARCHAR(15) NOT NULL,
                           Ten VARCHAR(15) NOT NULL,
                           TenDem VARCHAR(50),
                           Ho VARCHAR(50) NOT NULL,
                           NgaySinh DATE NOT NULL,
                           SoDienThoai VARCHAR(10) NOT NULL,
                           GioiTinh INT NOT NULL,
                           Email VARCHAR(255) NOT NULL,
                           TaiKhoan VARCHAR(100) NOT NULL,
                           MatKhau VARCHAR(100) NOT NULL,
                           DanToc VARCHAR(75),
                           DiaChi VARCHAR(255) NOT NULL,
                           Diem INT,
                           TrangThai INT,
                           FOREIGN KEY (ID_PhanLoai) REFERENCES PhanLoaiKhachHang(ID) ON DELETE SET NULL
);

-- Bảng KhoQua
CREATE TABLE KhoQua (
                        ID INT AUTO_INCREMENT PRIMARY KEY,
                        ID_KhachHang INT,
                        ID_Voucher INT,
                        FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID) ON DELETE CASCADE,
                        FOREIGN KEY (ID_Voucher) REFERENCES Voucher(ID) ON DELETE CASCADE
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
                        TrangThaiHoaDon INT,
                        FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID) ON DELETE CASCADE,
                        FOREIGN KEY (ID_Phim) REFERENCES Phim(ID) ON DELETE CASCADE,
                        FOREIGN KEY (ID_Voucher) REFERENCES Voucher(ID) ON DELETE CASCADE,
                        FOREIGN KEY (ID_PTTT) REFERENCES PTTT(ID) ON DELETE CASCADE
);

-- Bảng ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
                               ID INT AUTO_INCREMENT PRIMARY KEY,
                               ID_HoaDon INT,
                               ID_Ghe INT,
                               TrangThaiChiTietHoaDon INT,
                               FOREIGN KEY (ID_HoaDon) REFERENCES HoaDon(ID) ON DELETE CASCADE,
                               FOREIGN KEY (ID_Ghe) REFERENCES Ghe(ID) ON DELETE CASCADE
);

CREATE TABLE DoTuoi(
                       ID INT PRIMARY KEY AUTO_INCREMENT,
                       MaDoTuoi VARCHAR(25),
                       TenDoTuoi VARCHAR(5)
);
ALTER TABLE PHIM
    ADD CONSTRAINT `FK_PHIM_DOTUOI` FOREIGN KEY (GioiHanDoTuoi) REFERENCES dotuoi (ID);

-- Thêm dữ liệu vào bảng PhanLoaiChucVu
INSERT INTO PhanLoaiChucVu (MaChucVu, TenChucVu, TrangThaiPhanLoaiChucVu)
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

-- Thêm dữ liệu vào bảng PhanLoaiKhachHang
INSERT INTO PhanLoaiKhachHang (MaKhachHang, LoaiKhachHang, TrangThaiPhanLoaiKhachHang)
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

-- Thêm dữ liệu vào bảng Voucher
INSERT INTO Voucher (MaVoucher, GiaTriDoi, TruTienPhanTram, TruTienSo, SoTienGiam, SoTienToiThieu, GiamToiDa, NgayBatDau, NgayKetThuc, TrangThaiVoucher)
VALUES
    ('VC001', 500, 10, NULL, 50000, 100000, 50000, '2024-01-01', '2024-12-31', 1),
    ('VC002', 1000, NULL, 20000, 0, 50000, 20000, '2024-01-01', '2024-06-30', 1),
    ('VC003', 700, 5, NULL, 100000, 200000, 100000, '2024-02-01', '2024-05-31', 1);


-- Thêm dữ liệu vào bảng PhongChieu
INSERT INTO PhongChieu (MaPhong, TongSoGhe, TrangThaiPhongChieu)
VALUES
    ('PC001', 100, 1),
    ('PC002', 150, 1),
    ('PC003', 200, 1);

-- Thêm dữ liệu vào bảng Ghe
INSERT INTO Ghe (MaGhe, GiaTien, ID_PhongChieu, TrangThaiGhe)
VALUES
    ('GHE001', 50000, 1, 1),
    ('GHE002', 50000, 1, 1),
    ('GHE003', 50000, 1, 1),
    ('GHE004', 50000, 1, 1),
    ('GHE005', 50000, 1, 1),
    ('GHE006', 50000, 1, 1),
    ('GHE007', 50000, 2, 1),
    ('GHE008', 50000, 2, 1),
    ('GHE009', 50000, 2, 1),
    ('GHE010', 50000, 2, 1);

-- Thêm dữ liệu vào bảng SuatChieu

-- Thêm dữ liệu vào bảng TheLoaiPhim
INSERT INTO TheLoaiPhim (MaTLPhim, TenTheLoai, TrangThai)
VALUES
    ('TL001', 'Hành động', 1),
    ('TL002', 'Hài hước', 1),
    ('TL003', 'Kinh dị', 1),
    ('TL004', 'Tâm lý', 1),
    ('TL005', 'Tình cảm', 1);


INSERT INTO PhanLoaiChucVu (MaChucVu, TenChucVu, TrangThaiPhanLoaiChucVu)
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

-- Thêm dữ liệu vào bảng PhanLoaiKhachHang
INSERT INTO PhanLoaiKhachHang (MaKhachHang, LoaiKhachHang, TrangThaiPhanLoaiKhachHang)
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

-- Thêm dữ liệu vào bảng Voucher
INSERT INTO Voucher (MaVoucher, GiaTriDoi, TruTienPhanTram, TruTienSo, SoTienGiam, SoTienToiThieu, GiamToiDa, NgayBatDau, NgayKetThuc, TrangThaiVoucher)
VALUES
    ('VC001', 500, 10, NULL, 50000, 100000, 50000, '2024-01-01', '2024-12-31', 1),
    ('VC002', 1000, NULL, 20000, 0, 50000, 20000, '2024-01-01', '2024-06-30', 1),
    ('VC003', 700, 5, NULL, 100000, 200000, 100000, '2024-02-01', '2024-05-31', 1);


-- Thêm dữ liệu vào bảng PhongChieu
INSERT INTO PhongChieu (MaPhong, TongSoGhe, TrangThaiPhongChieu)
VALUES
    ('PC001', 100, 1),
    ('PC002', 150, 1),
    ('PC003', 200, 1);

-- Thêm dữ liệu vào bảng Ghe
INSERT INTO Ghe (MaGhe, GiaTien, ID_PhongChieu, TrangThaiGhe)
VALUES
    ('GHE001', 50000, 1, 1),
    ('GHE002', 50000, 1, 1),
    ('GHE003', 50000, 1, 1),
    ('GHE004', 50000, 1, 1),
    ('GHE005', 50000, 1, 1),
    ('GHE006', 50000, 1, 1),
    ('GHE007', 50000, 2, 1),
    ('GHE008', 50000, 2, 1),
    ('GHE009', 50000, 2, 1),
    ('GHE010', 50000, 2, 1);

-- Thêm dữ liệu vào bảng SuatChieu

-- Thêm dữ liệu vào bảng TheLoaiPhim
INSERT INTO TheLoaiPhim (MaTLPhim, TenTheLoai, TrangThai)
VALUES
    ('TL001', 'Hành động', 1),
    ('TL002', 'Hài hước', 1),
    ('TL003', 'Kinh dị', 1),
    ('TL004', 'Tâm lý', 1),
    ('TL005', 'Tình cảm', 1);

-- Thêm dữ liệu vào bảng DanhSachTLPhim

-- Thêm dữ liệu vào bảng PTTT
INSERT INTO PTTT (MaPTTT, TenPTTT, TrangThaiPTTT)
VALUES
    ('PT001', 'Tiền mặt', 1),
    ('PT002', 'Thẻ tín dụng', 1),
    ('PT003', 'Chuyển khoản', 1);

-- Thêm dữ liệu vào bảng NhanVien
INSERT INTO NhanVien (ID_ChucVu, MaNhanVien, Ten, TenDem, Ho, NgaySinh, GioiTinh, Email, TaiKhoan, MatKhau, DanToc, DiaChi, TrangThaiNhanVien)
VALUES
    (1, 'NV001', 'Nguyen', 'Van', 'A', '1990-01-01', 1, 'nv1@example.com', 'taikhoan1', 'matkhau1', 'Kinh', 'Dia chi 1', 1),
    (2, 'NV002', 'Le', 'Thi', 'B', '1992-02-02', 0, 'nv2@example.com', 'taikhoan2', 'matkhau2', 'Kinh', 'Dia chi 2', 1);

-- Thêm dữ liệu vào bảng KhachHang
INSERT INTO KhachHang (ID_PhanLoai, MaKhachHang, Ten, TenDem, Ho, NgaySinh, SoDienThoai, GioiTinh, Email, TaiKhoan, MatKhau, DanToc, DiaChi, Diem, TrangThai)
VALUES
    (1, 'KH001', 'Nguyen', 'Van', 'D', '1995-05-05', '0900000001', 1, 'kh1@example.com', 'kh1', 'mk1', 'Kinh', 'Dia chi khach 1', 100, 1),
    (2, 'KH002', 'Le', 'Thi', 'E', '1996-06-06', '0900000002', 0, 'kh2@example.com', 'kh2', 'mk2', 'Kinh', 'Dia chi khach 2', 200, 1);

-- Thêm dữ liệu vào bảng KhoQua
INSERT INTO KhoQua (ID_KhachHang, ID_Voucher)
VALUES
    (1, 1),
    (2, 2);



INSERT INTO DoTuoi(ID,MaDoTuoi,TenDoTuoi) VALUES (1,"DT001","R15");
INSERT INTO DoTuoi(ID,MaDoTuoi,TenDoTuoi)  VALUES (2,"DT002","R18");
INSERT INTO DoTuoi(ID,MaDoTuoi,TenDoTuoi)  VALUES (3, "DT003","R13");

-- Thêm dữ liệu vào bảng Phim
INSERT INTO Phim (MaPhim, TenPhim, AnhPhim, DienVien, Nam, NoiDungMoTa, Trailer, NgayRaMat, ThoiLuong, QuocGia, NoiDung, GioiHanDoTuoi, TrangThai)
VALUES
    ('PH001', 'Phim A', 'anhphim_a.jpg', 'Dien Vien A', 2023, 'Mo ta phim A', 'trailer_a.mp4', '30/12/2024', 120, 'Viet Nam', 'Noi dung phim A', 1, 1),
    ('PH002', 'Phim B', 'anhphim_b.jpg', 'Dien Vien B', 2022, 'Mo ta phim B', 'trailer_b.mp4', '28/10/2024', 150, 'My', 'Noi dung phim B', 2, 1),
    ('PH003', 'Phim C', 'anhphim_c.jpg', 'Dien Vien C', 2024, 'Mo ta phim C', 'trailer_c.mp4', '14/10/2024', 90, 'Han Quoc', 'Noi dung phim C', 2, 1);

INSERT INTO SuatChieu (MaSuatChieu, ThoiGianChieu, ID_Phim, ID_PhongChieu, TrangThaiSuatChieu)
VALUES
    ('SC001', '2024-01-01 18:00:00', 1, 1, 1),
    ('SC002', '2024-01-02 20:00:00', 2, 1, 1),
    ('SC003', '2024-01-03 21:00:00', 3, 2, 1);
INSERT INTO DanhSachTLPhim (ID_Phim, ID_TLPhim, TrangThai)
VALUES
    (1, 1, 1),
    (1, 2, 1),
    (2, 1, 1),
    (2, 3, 1),
    (3, 4, 1),
    (3, 5, 1);
-- Thêm dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (ID_KhachHang, ID_Phim, ID_Voucher, ID_PTTT, MaHoaDon, SoLuong, ThoiGianThanhToan, Diem, TongSoTien, TrangThaiHoaDon)
VALUES
    (1, 1, 1, 1, 'HD001', 2, '2024-01-01 19:00:00', 10, 100000, 1),
    (2, 2, 2, 2, 'HD002', 1, '2024-01-02 21:00:00', 20, 200000, 1);

-- Thêm dữ liệu vào bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (ID_HoaDon, ID_Ghe, TrangThaiChiTietHoaDon)
VALUES
    (1, 1, 1),
    (1, 2, 1),
    (2, 3, 1),
    (2, 4, 1);