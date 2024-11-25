-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 25, 2024 at 03:09 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shanksdsnitch`
--

-- --------------------------------------------------------

--
-- Table structure for table `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
                                 `ID` int(11) NOT NULL,
                                 `ID_HoaDon` int(11) DEFAULT NULL,
                                 `ID_Ghe` int(11) DEFAULT NULL,
                                 `TrangThaiChiTietHoaDon` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`ID`, `ID_HoaDon`, `ID_Ghe`, `TrangThaiChiTietHoaDon`) VALUES
                                                                                        (1, 1, 8, 2),
                                                                                        (2, 1, 2, 1),
                                                                                        (3, 2, 3, 1),
                                                                                        (4, 2, 4, 1),
                                                                                        (19, 1, 7, 0),
                                                                                        (38, 33, 13, 0),
                                                                                        (39, 33, 14, 0),
                                                                                        (40, 34, 13, 0),
                                                                                        (41, 34, 14, 0),
                                                                                        (42, 35, 14, 1),
                                                                                        (43, 36, 12, 0),
                                                                                        (44, 37, 12, 0),
                                                                                        (45, 38, 12, 0),
                                                                                        (46, 39, 12, 0),
                                                                                        (47, 40, 7, 1),
                                                                                        (48, 40, 8, 1),
                                                                                        (49, 41, 14, 0),
                                                                                        (50, 42, 14, 0),
                                                                                        (51, 43, 14, 0),
                                                                                        (52, 44, 14, 0),
                                                                                        (53, 45, 14, 0),
                                                                                        (54, 46, 14, 0),
                                                                                        (55, 47, 14, 0),
                                                                                        (56, 48, 14, 0),
                                                                                        (57, 49, 14, 0),
                                                                                        (58, 50, 14, 0),
                                                                                        (59, 51, 14, 0),
                                                                                        (60, 52, 14, 0),
                                                                                        (61, 53, 14, 0),
                                                                                        (62, 54, 14, 0),
                                                                                        (63, 55, 14, 0);

-- --------------------------------------------------------

--
-- Table structure for table `danhsachtlphim`
--

CREATE TABLE `danhsachtlphim` (
                                  `ID` int(11) NOT NULL,
                                  `ID_Phim` int(11) DEFAULT NULL,
                                  `ID_TLPhim` int(11) DEFAULT NULL,
                                  `TrangThai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `danhsachtlphim`
--

INSERT INTO `danhsachtlphim` (`ID`, `ID_Phim`, `ID_TLPhim`, `TrangThai`) VALUES
                                                                             (1, 1, 1, 1),
                                                                             (2, 1, 2, 1),
                                                                             (3, 2, 1, 1),
                                                                             (4, 2, 3, 1),
                                                                             (5, 3, 4, 1),
                                                                             (6, 3, 5, 1);

-- --------------------------------------------------------

--
-- Table structure for table `dotuoi`
--

CREATE TABLE `dotuoi` (
                          `ID` int(11) NOT NULL,
                          `MaDoTuoi` varchar(25) DEFAULT NULL,
                          `TenDoTuoi` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dotuoi`
--

INSERT INTO `dotuoi` (`ID`, `MaDoTuoi`, `TenDoTuoi`) VALUES
                                                         (1, 'DT001', 'R15'),
                                                         (2, 'DT002', 'R18'),
                                                         (3, 'DT003', 'R13'),
                                                         (4, 'MD25', 'R25'),
                                                         (5, 'DT296', 'R20'),
                                                         (6, 'DT003', 'R200');

-- --------------------------------------------------------

--
-- Table structure for table `ghe`
--

CREATE TABLE `ghe` (
                       `ID` int(11) NOT NULL,
                       `MaGhe` varchar(15) NOT NULL,
                       `GiaTien` decimal(18,2) NOT NULL,
                       `ID_PhongChieu` int(11) DEFAULT NULL,
                       `TrangThaiGhe` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ghe`
--

INSERT INTO `ghe` (`ID`, `MaGhe`, `GiaTien`, `ID_PhongChieu`, `TrangThaiGhe`) VALUES
                                                                                  (1, 'GheTestApi', 1000.00, 1, 0),
                                                                                  (2, 'GHE002', 520000.00, 1, 1),
                                                                                  (3, 'GHE003', 6890.00, 1, 1),
                                                                                  (4, 'GHE004', 9000.00, 1, 1),
                                                                                  (5, 'GHE005', 1000.00, 1, 1),
                                                                                  (6, 'GHE006', 50000.00, 1, 1),
                                                                                  (7, 'GHE007', 50000.00, 2, 1),
                                                                                  (8, 'GHE008', 50000.00, 2, 1),
                                                                                  (12, 'GheTestAPEEPEE', 1000.00, 1, 0),
                                                                                  (13, 'GH0012', 1000.00, 1, 0),
                                                                                  (14, 'Star', 1000.00, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `hoadon`
--

CREATE TABLE `hoadon` (
                          `ID` int(11) NOT NULL,
                          `ID_KhachHang` int(11) DEFAULT NULL,
                          `ID_SuatChieu` int(11) DEFAULT NULL,
                          `ID_Voucher` int(11) DEFAULT NULL,
                          `ID_PTTT` int(11) DEFAULT NULL,
                          `MaHoaDon` varchar(15) NOT NULL,
                          `SoLuong` int(11) NOT NULL,
                          `ThoiGianThanhToan` datetime DEFAULT NULL,
                          `Diem` int(11) NOT NULL,
                          `TongSoTien` decimal(18,2) NOT NULL,
                          `TrangThaiHoaDon` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hoadon`
--

INSERT INTO `hoadon` (`ID`, `ID_KhachHang`, `ID_SuatChieu`, `ID_Voucher`, `ID_PTTT`, `MaHoaDon`, `SoLuong`, `ThoiGianThanhToan`, `Diem`, `TongSoTien`, `TrangThaiHoaDon`) VALUES
                                                                                                                                                                              (1, 1, 1, NULL, 5, 'HD001', 2, '2024-01-01 19:00:00', 10, 1000.00, 2),
                                                                                                                                                                              (2, 2, 2, NULL, 2, 'HD002', 1, '2024-01-02 21:00:00', 20, 15890.00, 1),
                                                                                                                                                                              (3, 1, 2, 1, 2, 'HD001', 2, '2024-01-01 19:00:00', 10, 1220000.00, 0),
                                                                                                                                                                              (4, 1, 2, 1, 2, 'HD001', 2, '2024-01-01 19:00:00', 10, 1170000.00, 0),
                                                                                                                                                                              (5, 1, 2, NULL, NULL, 'HD004', 2, '2024-01-01 19:00:00', 12210, 0.00, 0),
                                                                                                                                                                              (6, 1, 2, 1, NULL, 'HD005', 2, '2024-01-01 19:00:00', 12210, 0.00, 0),
                                                                                                                                                                              (7, 1, 2, 1, NULL, 'HD006', 2, '2024-01-01 19:00:00', 12210, 0.00, 0),
                                                                                                                                                                              (8, 1, 2, 1, NULL, 'HD007', 2, '2024-01-01 19:00:00', 12210, 0.00, 0),
                                                                                                                                                                              (9, 1, 2, 1, NULL, 'HD008', 2, '2024-11-14 15:27:03', 12210, 1000.00, 1),
                                                                                                                                                                              (30, 1, 1, NULL, NULL, 'HD030', 2, '2024-11-19 09:17:07', 0, 1000.00, 0),
                                                                                                                                                                              (31, 1, 1, NULL, NULL, 'HD031', 2, '2024-11-19 09:17:07', 0, 2000.00, 0),
                                                                                                                                                                              (32, 1, 1, NULL, NULL, 'HD032', 2, '2024-11-19 09:17:07', 0, 2000.00, 0),
                                                                                                                                                                              (33, 1, 1, NULL, NULL, 'HD033', 2, '2024-11-19 09:17:07', 0, 2000.00, 0),
                                                                                                                                                                              (34, 1, 1, NULL, NULL, 'HD0033', 2, '2024-11-19 09:46:45', 0, 2000.00, 0),
                                                                                                                                                                              (35, 2, 2, NULL, NULL, 'HD0034', 1, '2024-11-19 09:58:20', 0, 1000.00, 1),
                                                                                                                                                                              (36, 2, 2, NULL, NULL, 'HD0035', 1, '2024-11-19 10:10:06', 0, 0.00, 0),
                                                                                                                                                                              (37, 2, 2, NULL, NULL, 'HD0036', 1, '2024-11-19 10:11:49', 0, 0.00, 0),
                                                                                                                                                                              (38, 2, 2, NULL, NULL, 'HD0037', 1, '2024-11-19 10:13:32', 0, 0.00, 0),
                                                                                                                                                                              (39, 2, 2, NULL, NULL, 'HD0038', 1, '2024-11-19 10:16:07', 10, 1000.00, 0),
                                                                                                                                                                              (40, 1, 2, 2, 1, 'HD0039', 2, '2024-11-19 14:57:21', 1000, 87480.00, 1),
                                                                                                                                                                              (41, 1, 1, NULL, NULL, 'HD0040', 1, '2024-11-20 09:19:22', 10, 1000.00, 0),
                                                                                                                                                                              (42, 1, 1, NULL, NULL, 'HD0041', 1, '2024-11-20 09:51:20', 10, 1000.00, 0),
                                                                                                                                                                              (43, 1, 1, NULL, NULL, 'HD0042', 1, '2024-11-20 09:53:00', 10, 1000.00, 0),
                                                                                                                                                                              (44, 1, 1, NULL, NULL, 'HD0043', 1, '2024-11-20 09:54:03', 10, 1000.00, 0),
                                                                                                                                                                              (45, 1, 1, NULL, NULL, 'HD0044', 1, '2024-11-20 10:00:11', 10, 1000.00, 0),
                                                                                                                                                                              (46, 1, 1, NULL, NULL, 'HD0045', 1, '2024-11-20 10:03:41', 10, 1000.00, 0),
                                                                                                                                                                              (47, 1, 1, NULL, NULL, 'HD0046', 1, '2024-11-20 10:09:50', 10, 1000.00, 0),
                                                                                                                                                                              (48, 1, 1, NULL, NULL, 'HD0047', 1, '2024-11-20 10:15:12', 10, 1000.00, 0),
                                                                                                                                                                              (49, 1, 1, NULL, NULL, 'HD0048', 1, '2024-11-20 10:20:22', 10, 1000.00, 0),
                                                                                                                                                                              (50, 1, 3, NULL, NULL, 'HD0049', 1, '2024-11-20 10:24:13', 10, 967000.00, 1),
                                                                                                                                                                              (51, 1, 3, NULL, NULL, 'HD0050', 1, '2024-11-22 09:39:43', 10, 1000.00, 0),
                                                                                                                                                                              (52, 1, 3, NULL, 1, 'HD0051', 1, '2024-11-22 10:26:53', 10, 1000.00, 0),
                                                                                                                                                                              (53, 1, 3, NULL, 1, 'HD0052', 1, '2024-11-22 10:52:45', 10, 1000.00, 0),
                                                                                                                                                                              (54, 1, 3, NULL, 1, 'HD0053', 1, '2024-11-22 11:03:50', 10, 1000.00, 0),
                                                                                                                                                                              (55, 2, 3, NULL, 1, 'HD0054', 1, '2024-11-22 11:06:43', 10, 1000.00, 0);

-- --------------------------------------------------------

--
-- Table structure for table `khachhang`
--

CREATE TABLE `khachhang` (
                             `ID` int(11) NOT NULL,
                             `ID_PhanLoai` int(11) DEFAULT NULL,
                             `ID_TaiKhoan` int(11) DEFAULT NULL,
                             `MaKhachHang` varchar(15) NOT NULL,
                             `Ten` varchar(15) NOT NULL,
                             `TenDem` varchar(50) DEFAULT NULL,
                             `Ho` varchar(50) NOT NULL,
                             `NgaySinh` date NOT NULL,
                             `SoDienThoai` varchar(10) NOT NULL,
                             `GioiTinh` int(11) NOT NULL,
                             `Email` varchar(255) NOT NULL,
                             `DanToc` varchar(75) DEFAULT NULL,
                             `DiaChi` varchar(255) NOT NULL,
                             `Diem` int(11) DEFAULT NULL,
                             `TrangThaiKhachHang` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `khachhang`
--

INSERT INTO `khachhang` (`ID`, `ID_PhanLoai`, `ID_TaiKhoan`, `MaKhachHang`, `Ten`, `TenDem`, `Ho`, `NgaySinh`, `SoDienThoai`, `GioiTinh`, `Email`, `DanToc`, `DiaChi`, `Diem`, `TrangThaiKhachHang`) VALUES
                                                                                                                                                                                                         (1, 1, 3, 'KH001', 'East', 'D', 'Look', '1995-05-05', '0900000001', 1, 'kh1@example.com', 'Kinh', 'Dia chi khach 1', 1011509, 1),
                                                                                                                                                                                                         (2, 2, 4, 'KH002', 'Le', 'Thi', 'E', '1996-06-06', '0900000002', 0, 'kh2@example.com', 'Kinh', 'Dia chi khach 2', 50, 1),
                                                                                                                                                                                                         (3, 1, 5, 'KH003', 'Rat', 'D', 'Shanks', '2024-11-08', '0192856725', 0, 'shanks@ratmail.com', 'Kinh', '5 Bailey Str.', 50, 0),
                                                                                                                                                                                                         (4, 1, 6, 'KH004', 'Stairs', 'D', 'Down', '2024-11-13', '0127682456', 0, 'DownDStairs@email.com', 'Kinh', 'East Blue', 0, 0),
                                                                                                                                                                                                         (5, 1, 11, 'KH005', 'string', 'string', 'string', '2024-11-13', '0908997890', 0, 'string@gmail.com', 'string', 'string', 0, 1);

-- --------------------------------------------------------

--
-- Stand-in structure for view `khachhangrevenune`
-- (See below for the actual view)
--
CREATE TABLE `khachhangrevenune` (
                                     `ID` int(11)
    ,`Ten` varchar(117)
    ,`MaTaiKhoan` varchar(20)
    ,`gioiTinh` int(11)
    ,`Diem` int(11)
    ,`TongSoTien` decimal(40,2)
);

-- --------------------------------------------------------

--
-- Table structure for table `khoqua`
--

CREATE TABLE `khoqua` (
                          `ID` int(11) NOT NULL,
                          `ID_KhachHang` int(11) DEFAULT NULL,
                          `ID_Voucher` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `khoqua`
--

INSERT INTO `khoqua` (`ID`, `ID_KhachHang`, `ID_Voucher`) VALUES
                                                              (1, 1, 1),
                                                              (2, 2, 2),
                                                              (3, 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `nhanvien`
--

CREATE TABLE `nhanvien` (
                            `ID` int(11) NOT NULL,
                            `ID_ChucVu` int(11) DEFAULT NULL,
                            `ID_TaiKhoan` int(11) DEFAULT NULL,
                            `MaNhanVien` varchar(15) NOT NULL,
                            `Ten` varchar(100) NOT NULL,
                            `TenDem` varchar(100) NOT NULL,
                            `Ho` varchar(50) NOT NULL,
                            `NgaySinh` date NOT NULL,
                            `GioiTinh` int(11) NOT NULL,
                            `Email` varchar(100) NOT NULL,
                            `DanToc` varchar(50) DEFAULT NULL,
                            `DiaChi` varchar(255) NOT NULL,
                            `TrangThaiNhanVien` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhanvien`
--

INSERT INTO `nhanvien` (`ID`, `ID_ChucVu`, `ID_TaiKhoan`, `MaNhanVien`, `Ten`, `TenDem`, `Ho`, `NgaySinh`, `GioiTinh`, `Email`, `DanToc`, `DiaChi`, `TrangThaiNhanVien`) VALUES
                                                                                                                                                                             (1, 1, 1, 'NV001', 'Dragon', 'Is A Fraud', 'frfr', '1990-01-01', 1, 'nv1@example.com', 'Kinh', 'Dia chi 1', 1),
                                                                                                                                                                             (2, 2, 2, 'NV002', 'Le', 'Thi', 'B', '1992-02-02', 0, 'nv2@example.com', 'Kinh', 'Dia chi 2', 1),
                                                                                                                                                                             (7, 7, 12, 'NV003', 'Save Me', 'Too Hungry', 'I\'m not even a tester', '2024-11-13', 0, 'fml@email.com', 'Whatever', 'Earth', 1);

-- --------------------------------------------------------

--
-- Table structure for table `phanloaichucvu`
--

CREATE TABLE `phanloaichucvu` (
                                  `ID` int(11) NOT NULL,
                                  `MaChucVu` varchar(15) NOT NULL,
                                  `TenChucVu` varchar(150) NOT NULL,
                                  `TrangThaiPhanLoaiChucVu` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phanloaichucvu`
--

INSERT INTO `phanloaichucvu` (`ID`, `MaChucVu`, `TenChucVu`, `TrangThaiPhanLoaiChucVu`) VALUES
                                                                                            (1, 'CV001', 'Quản lý', 1),
                                                                                            (2, 'CV002', 'Nhân viên bán vé', 1),
                                                                                            (3, 'CV003', 'Nhân viên kỹ thuật', 1),
                                                                                            (4, 'CV004', 'Nhân viên chăm sóc khách hàng', 1),
                                                                                            (5, 'CV005', 'Quản trị viên', 1),
                                                                                            (6, 'CV006', 'Nhân viên vệ sinh', 1),
                                                                                            (7, 'CV007', 'Bảo vệ', 1),
                                                                                            (8, 'CV008', 'Nhân viên thu ngân', 1),
                                                                                            (9, 'CV009', 'Nhân viên kế toán', 1),
                                                                                            (10, 'CV010', 'Nhân viên Marketing', 1);

-- --------------------------------------------------------

--
-- Table structure for table `phanloaikhachhang`
--

CREATE TABLE `phanloaikhachhang` (
                                     `ID` int(11) NOT NULL,
                                     `MaKhachHang` varchar(15) NOT NULL,
                                     `TenPhanLoaiKhachHang` varchar(100) NOT NULL,
                                     `TrangThaiPhanLoaiKhachHang` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phanloaikhachhang`
--

INSERT INTO `phanloaikhachhang` (`ID`, `MaKhachHang`, `TenPhanLoaiKhachHang`, `TrangThaiPhanLoaiKhachHang`) VALUES
                                                                                                                (1, 'KH001', 'Thành viên thường', 1),
                                                                                                                (2, 'KH002', 'Thành viên VIP', 1),
                                                                                                                (3, 'KH003', 'Thành viên Platinum', 1),
                                                                                                                (4, 'KH004', 'Thành viên Vàng', 1),
                                                                                                                (5, 'KH005', 'Thành viên Bạc', 1),
                                                                                                                (6, 'KH006', 'Thành viên Đồng', 1),
                                                                                                                (7, 'KH007', 'Thành viên Sinh viên', 1),
                                                                                                                (8, 'KH008', 'Thành viên Gia đình', 1),
                                                                                                                (9, 'KH009', 'Thành viên Doanh nghiệp', 1),
                                                                                                                (10, 'KH010', 'Thành viên Khách mời', 1);

-- --------------------------------------------------------

--
-- Table structure for table `phanloaitaikhoan`
--

CREATE TABLE `phanloaitaikhoan` (
                                    `ID` int(11) NOT NULL,
                                    `TenLoaiTaiKhoan` varchar(50) NOT NULL,
                                    `TrangThaiPhanLoaiTaiKhoan` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phanloaitaikhoan`
--

INSERT INTO `phanloaitaikhoan` (`ID`, `TenLoaiTaiKhoan`, `TrangThaiPhanLoaiTaiKhoan`) VALUES
                                                                                          (1, 'NhanVien', 1),
                                                                                          (2, 'KhachHang', 1);

-- --------------------------------------------------------

--
-- Table structure for table `phim`
--

CREATE TABLE `phim` (
                        `ID` int(11) NOT NULL,
                        `MaPhim` varchar(15) NOT NULL,
                        `TenPhim` varchar(255) NOT NULL,
                        `AnhPhim` varchar(255) NOT NULL,
                        `DienVien` varchar(255) NOT NULL,
                        `Nam` int(11) NOT NULL,
                        `NoiDungMoTa` text DEFAULT NULL,
                        `Trailer` varchar(255) DEFAULT NULL,
                        `NgayRaMat` date NOT NULL,
                        `ThoiLuong` int(11) NOT NULL,
                        `QuocGia` varchar(100) NOT NULL,
                        `NoiDung` text DEFAULT NULL,
                        `GioiHanDoTuoi` int(11) NOT NULL,
                        `TrangThai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phim`
--

INSERT INTO `phim` (`ID`, `MaPhim`, `TenPhim`, `AnhPhim`, `DienVien`, `Nam`, `NoiDungMoTa`, `Trailer`, `NgayRaMat`, `ThoiLuong`, `QuocGia`, `NoiDung`, `GioiHanDoTuoi`, `TrangThai`) VALUES
                                                                                                                                                                                         (1, 'PH001', 'Phim A', 'anhphim_a.jpg', 'Dien Vien A', 2023, 'Mo ta phim A', 'trailer_a.mp4', '2024-01-01', 120, 'Viet Nam', 'Noi dung phim A', 1, 1),
                                                                                                                                                                                         (2, 'PH002', 'Phim B', 'anhphim_b.jpg', 'Dien Vien B', 2022, 'Mo ta phim B', 'trailer_b.mp4', '2024-01-01', 150, 'My', 'Noi dung phim B', 2, 1),
                                                                                                                                                                                         (3, 'PH003', 'Phim C', 'anhphim_c.jpg', 'Dien Vien C', 2024, 'Mo ta phim C', 'trailer_c.mp4', '2024-01-01', 90, 'Han Quoc', 'Noi dung phim C', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `phongchieu`
--

CREATE TABLE `phongchieu` (
                              `ID` int(11) NOT NULL,
                              `MaPhong` varchar(15) NOT NULL,
                              `TongSoGhe` int(11) NOT NULL,
                              `TrangThaiPhongChieu` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phongchieu`
--

INSERT INTO `phongchieu` (`ID`, `MaPhong`, `TongSoGhe`, `TrangThaiPhongChieu`) VALUES
                                                                                   (1, 'PC001', 100, 1),
                                                                                   (2, 'PC002', 150, 1),
                                                                                   (3, 'PC003', 200, 1),
                                                                                   (4, 'PhongAn', 20, 0),
                                                                                   (5, 'PhongAn', 20, 0),
                                                                                   (6, 'PC005', 20, 0);

-- --------------------------------------------------------

--
-- Table structure for table `pttt`
--

CREATE TABLE `pttt` (
                        `ID` int(11) NOT NULL,
                        `MaPTTT` varchar(15) NOT NULL,
                        `TenPTTT` varchar(200) NOT NULL,
                        `TrangThaiPTTT` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pttt`
--

INSERT INTO `pttt` (`ID`, `MaPTTT`, `TenPTTT`, `TrangThaiPTTT`) VALUES
                                                                    (1, 'PT001', 'Tiền mặtt', 1),
                                                                    (2, 'PT002', 'Thẻ tín dụng', 1),
                                                                    (3, 'PT003', 'Chuyển khoản', 1),
                                                                    (4, 'PTTT5', 'Vay nợ', 0),
                                                                    (5, 'PTWhat', 'R92', 1);

-- --------------------------------------------------------

--
-- Table structure for table `suatchieu`
--

CREATE TABLE `suatchieu` (
                             `ID` int(11) NOT NULL,
                             `MaSuatChieu` varchar(15) NOT NULL,
                             `ThoiGianChieu` datetime NOT NULL,
                             `ID_Phim` int(11) DEFAULT NULL,
                             `ID_PhongChieu` int(11) DEFAULT NULL,
                             `TrangThaiSuatChieu` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suatchieu`
--

INSERT INTO `suatchieu` (`ID`, `MaSuatChieu`, `ThoiGianChieu`, `ID_Phim`, `ID_PhongChieu`, `TrangThaiSuatChieu`) VALUES
                                                                                                                     (1, 'MSC005', '2025-01-01 18:00:00', 1, 1, 1),
                                                                                                                     (2, 'SC002', '2024-01-02 20:00:00', 2, 1, 1),
                                                                                                                     (3, 'SC003', '2024-01-03 21:00:00', 3, 2, 1),
                                                                                                                     (4, 'MSC004', '2024-01-01 18:00:00', 1, 1, 1),
                                                                                                                     (5, 'MSC005', '2024-01-01 18:00:00', 1, 1, 1),
                                                                                                                     (6, 'MSC006', '2025-01-01 18:00:01', 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `taikhoan`
--

CREATE TABLE `taikhoan` (
                            `ID` int(11) NOT NULL,
                            `MaTaiKhoan` varchar(20) GENERATED ALWAYS AS (concat('TK00',`ID`)) VIRTUAL,
                            `TenDangNhap` varchar(100) NOT NULL,
                            `MatKhau` varchar(100) NOT NULL,
                            `TrangThaiTaiKhoan` int(11) NOT NULL,
                            `ID_PhanLoaiTaiKhoan` int(11) NOT NULL,
                            `OTP` varchar(50) DEFAULT NULL,
                            `GhiChu` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `taikhoan`
--

INSERT INTO `taikhoan` (`ID`, `TenDangNhap`, `MatKhau`, `TrangThaiTaiKhoan`, `ID_PhanLoaiTaiKhoan`, `OTP`, `GhiChu`) VALUES
                                                                                                                         (1, 'tk1', '$2a$10$bOOwbY7XlEX1yU0hj5KaxuusAY7PqV0rf9NFUXMssrnoGC8lc0kSC', 1, 1, '', 'Fifth user'),
                                                                                                                         (2, 'tk2', '$2a$10$VVXhYwpxUdTQrLpURl57UO0H89kbTKvosB8YCPQqe2lVZTTIJ8m6q', 1, 1, '', 'Sixth user'),
                                                                                                                         (3, 'tk3', '$2a$10$Uko2FFUAYGOF1LQxzDM6Ve77ZGgj0M3AMxO5mBE4ZiuFhsUN5hG0.', 0, 2, '', 'Seventh user'),
                                                                                                                         (4, 'tk4', '$2a$10$eFvzrIwkKVKTRkGSpesrbOjLoDHD4MglEyJ9Y7csrvbMBAiSrxA8O', 0, 2, '', 'Eighth user'),
                                                                                                                         (5, 'tk5', '$2a$10$cMVCilb3.wLa7GqntLvdxOGKUTqVsqbaks/pwkM.dzVf8.LIpFxnW', 1, 2, '', ''),
                                                                                                                         (6, 'mk6', '$2a$10$MJbAM0LNIZx0kWA5SmAwcuPsAZYhMiQYXQU9bSbgEkyqYjbrETYfG', 0, 2, '', ''),
                                                                                                                         (11, 'tk8', '$2a$10$umWGAOXhaS8Z.MKf5eCta.lHo.zjDaUOYXbxI2pLZV32FH0xkJeRm', 1, 2, '', ''),
                                                                                                                         (12, 'tk7', '$2a$10$1iPL6RrmGBR7/ax0mY85o.8T2iWUso3ZpEq5FJcOf5yxy8WVg5Kfa', 1, 1, '', '');

-- --------------------------------------------------------

--
-- Table structure for table `theloaiphim`
--

CREATE TABLE `theloaiphim` (
                               `ID` int(11) NOT NULL,
                               `MaTLPhim` varchar(15) NOT NULL,
                               `TenTheLoai` varchar(100) NOT NULL,
                               `TrangThai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `theloaiphim`
--

INSERT INTO `theloaiphim` (`ID`, `MaTLPhim`, `TenTheLoai`, `TrangThai`) VALUES
                                                                            (1, 'R19+', 'Adultery', 1),
                                                                            (2, 'TL002', 'Hài hước', 1),
                                                                            (3, 'TL003', 'Kinh dị', 1),
                                                                            (4, 'TL004', 'Tâm lý', 1),
                                                                            (5, 'TL005', 'Tình cảm', 1),
                                                                            (6, 'TL001', 'Hành động', 1),
                                                                            (7, 'TL002', 'Hài hước', 1),
                                                                            (8, 'TL003', 'Kinh dị', 1),
                                                                            (9, 'TL004', 'Tâm lý', 1),
                                                                            (10, 'TL005', 'Tình cảm', 1),
                                                                            (11, 'TL0010', '?', 0),
                                                                            (12, 'TL0011', '??', 0);

-- --------------------------------------------------------

--
-- Table structure for table `voucher`
--

CREATE TABLE `voucher` (
                           `ID` int(11) NOT NULL,
                           `MaVoucher` varchar(15) NOT NULL,
                           `GiaTriDoi` int(11) NOT NULL,
                           `TruTienPhanTram` int(11) DEFAULT NULL,
                           `TruTienSo` decimal(18,2) DEFAULT NULL,
                           `SoTienToiThieu` decimal(18,2) DEFAULT NULL,
                           `GiamToiDa` decimal(18,2) DEFAULT NULL,
                           `NgayBatDau` date DEFAULT NULL,
                           `NgayKetThuc` date DEFAULT NULL,
                           `SoLuong` int(11) DEFAULT NULL,
                           `TrangThaiVoucher` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `voucher`
--

INSERT INTO `voucher` (`ID`, `MaVoucher`, `GiaTriDoi`, `TruTienPhanTram`, `TruTienSo`, `SoTienToiThieu`, `GiamToiDa`, `NgayBatDau`, `NgayKetThuc`, `SoLuong`, `TrangThaiVoucher`) VALUES
                                                                                                                                                                                      (1, 'VC001', 500, 10, NULL, 100000.00, 50000.00, '2024-01-01', '2024-12-31', 0, 1),
                                                                                                                                                                                      (2, 'VC002', 1000, NULL, 12520.00, 50000.00, 20000.00, '2024-01-01', '2025-06-30', 1, 1),
                                                                                                                                                                                      (3, 'VC003', 700, 5, NULL, 200000.00, 100000.00, '2024-02-01', '2024-12-22', 6, 1),
                                                                                                                                                                                      (4, 'TestVoucher15', 1, 1, NULL, 0.00, 1.00, '2024-11-12', '2024-11-12', 10, 1),
                                                                                                                                                                                      (5, 'TestVoucher16', 1, NULL, 1.00, 0.00, 1.00, '2023-11-01', '2024-11-28', 10, 1);

-- --------------------------------------------------------

--
-- Structure for view `khachhangrevenune`
--
DROP TABLE IF EXISTS `khachhangrevenune`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `khachhangrevenune`  AS SELECT `kh`.`ID` AS `ID`, concat(`kh`.`Ho`,' ',`kh`.`TenDem`,' ',`kh`.`Ten`) AS `Ten`, `tk`.`MaTaiKhoan` AS `MaTaiKhoan`, `kh`.`GioiTinh` AS `gioiTinh`, `kh`.`Diem` AS `Diem`, sum(`h`.`TongSoTien`) AS `TongSoTien` FROM ((`khachhang` `kh` join `taikhoan` `tk` on(`tk`.`ID` = `kh`.`ID_TaiKhoan`)) join `hoadon` `h` on(`kh`.`ID` = `h`.`ID_KhachHang`)) GROUP BY `kh`.`ID`, `tk`.`MaTaiKhoan` ORDER BY sum(`h`.`TongSoTien`) DESC ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_HoaDon` (`ID_HoaDon`),
    ADD KEY `ID_Ghe` (`ID_Ghe`);

--
-- Indexes for table `danhsachtlphim`
--
ALTER TABLE `danhsachtlphim`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Phim` (`ID_Phim`),
    ADD KEY `ID_TLPhim` (`ID_TLPhim`);

--
-- Indexes for table `dotuoi`
--
ALTER TABLE `dotuoi`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `ghe`
--
ALTER TABLE `ghe`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_PhongChieu` (`ID_PhongChieu`);

--
-- Indexes for table `hoadon`
--
ALTER TABLE `hoadon`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_KhachHang` (`ID_KhachHang`),
    ADD KEY `ID_Phim` (`ID_SuatChieu`),
    ADD KEY `ID_Voucher` (`ID_Voucher`),
    ADD KEY `ID_PTTT` (`ID_PTTT`);

--
-- Indexes for table `khachhang`
--
ALTER TABLE `khachhang`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_PhanLoai` (`ID_PhanLoai`),
    ADD KEY `ID_TaiKhoan` (`ID_TaiKhoan`);

--
-- Indexes for table `khoqua`
--
ALTER TABLE `khoqua`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_KhachHang` (`ID_KhachHang`),
    ADD KEY `ID_Voucher` (`ID_Voucher`);

--
-- Indexes for table `nhanvien`
--
ALTER TABLE `nhanvien`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_ChucVu` (`ID_ChucVu`),
    ADD KEY `ID_TaiKhoan` (`ID_TaiKhoan`);

--
-- Indexes for table `phanloaichucvu`
--
ALTER TABLE `phanloaichucvu`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `phanloaikhachhang`
--
ALTER TABLE `phanloaikhachhang`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `phanloaitaikhoan`
--
ALTER TABLE `phanloaitaikhoan`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `TenLoaiTaiKhoan` (`TenLoaiTaiKhoan`);

--
-- Indexes for table `phim`
--
ALTER TABLE `phim`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `FK_PHIM_DOTUOI` (`GioiHanDoTuoi`);

--
-- Indexes for table `phongchieu`
--
ALTER TABLE `phongchieu`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `pttt`
--
ALTER TABLE `pttt`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `suatchieu`
--
ALTER TABLE `suatchieu`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Phim` (`ID_Phim`),
    ADD KEY `ID_PhongChieu` (`ID_PhongChieu`);

--
-- Indexes for table `taikhoan`
--
ALTER TABLE `taikhoan`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `TenDangNhap` (`TenDangNhap`),
    ADD KEY `ID_PhanLoaiTaiKhoan` (`ID_PhanLoaiTaiKhoan`);

--
-- Indexes for table `theloaiphim`
--
ALTER TABLE `theloaiphim`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `voucher`
--
ALTER TABLE `voucher`
    ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `danhsachtlphim`
--
ALTER TABLE `danhsachtlphim`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `dotuoi`
--
ALTER TABLE `dotuoi`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `ghe`
--
ALTER TABLE `ghe`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `hoadon`
--
ALTER TABLE `hoadon`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT for table `khachhang`
--
ALTER TABLE `khachhang`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `khoqua`
--
ALTER TABLE `khoqua`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nhanvien`
--
ALTER TABLE `nhanvien`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `phanloaichucvu`
--
ALTER TABLE `phanloaichucvu`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `phanloaikhachhang`
--
ALTER TABLE `phanloaikhachhang`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `phanloaitaikhoan`
--
ALTER TABLE `phanloaitaikhoan`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `phim`
--
ALTER TABLE `phim`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `phongchieu`
--
ALTER TABLE `phongchieu`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `pttt`
--
ALTER TABLE `pttt`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `suatchieu`
--
ALTER TABLE `suatchieu`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `taikhoan`
--
ALTER TABLE `taikhoan`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `theloaiphim`
--
ALTER TABLE `theloaiphim`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `voucher`
--
ALTER TABLE `voucher`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
    ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`ID_HoaDon`) REFERENCES `hoadon` (`ID`) ,
    ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`ID_Ghe`) REFERENCES `ghe` (`ID`) ;

--
-- Constraints for table `danhsachtlphim`
--
ALTER TABLE `danhsachtlphim`
    ADD CONSTRAINT `danhsachtlphim_ibfk_1` FOREIGN KEY (`ID_Phim`) REFERENCES `phim` (`ID`),
    ADD CONSTRAINT `danhsachtlphim_ibfk_2` FOREIGN KEY (`ID_TLPhim`) REFERENCES `theloaiphim` (`ID`);

--
-- Constraints for table `ghe`
--
ALTER TABLE `ghe`
    ADD CONSTRAINT `ghe_ibfk_1` FOREIGN KEY (`ID_PhongChieu`) REFERENCES `phongchieu` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `hoadon`
--
ALTER TABLE `hoadon`
    ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`ID_KhachHang`) REFERENCES `khachhang` (`ID`),
    ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`ID_SuatChieu`) REFERENCES `suatchieu` (`ID`),
    ADD CONSTRAINT `hoadon_ibfk_3` FOREIGN KEY (`ID_Voucher`) REFERENCES `voucher` (`ID`),
    ADD CONSTRAINT `hoadon_ibfk_4` FOREIGN KEY (`ID_PTTT`) REFERENCES `pttt` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `khachhang`
--
ALTER TABLE `khachhang`
    ADD CONSTRAINT `khachhang_ibfk_1` FOREIGN KEY (`ID_PhanLoai`) REFERENCES `phanloaikhachhang` (`ID`) ON DELETE SET NULL,
    ADD CONSTRAINT `khachhang_ibfk_2` FOREIGN KEY (`ID_TaiKhoan`) REFERENCES `taikhoan` (`ID`) ON DELETE SET NULL;

--
-- Constraints for table `khoqua`
--
ALTER TABLE `khoqua`
    ADD CONSTRAINT `khoqua_ibfk_1` FOREIGN KEY (`ID_KhachHang`) REFERENCES `khachhang` (`ID`),
    ADD CONSTRAINT `khoqua_ibfk_2` FOREIGN KEY (`ID_Voucher`) REFERENCES `voucher` (`ID`);

--
-- Constraints for table `nhanvien`
--
ALTER TABLE `nhanvien`
    ADD CONSTRAINT `nhanvien_ibfk_1` FOREIGN KEY (`ID_ChucVu`) REFERENCES `phanloaichucvu` (`ID`) ON DELETE SET NULL,
    ADD CONSTRAINT `nhanvien_ibfk_2` FOREIGN KEY (`ID_TaiKhoan`) REFERENCES `taikhoan` (`ID`) ON DELETE SET NULL;

--
-- Constraints for table `phim`
--
ALTER TABLE `phim`
    ADD CONSTRAINT `FK_PHIM_DOTUOI` FOREIGN KEY (`GioiHanDoTuoi`) REFERENCES `dotuoi` (`ID`);

--
-- Constraints for table `suatchieu`
--
ALTER TABLE `suatchieu`
    ADD CONSTRAINT `suatchieu_ibfk_1` FOREIGN KEY (`ID_Phim`) REFERENCES `phim` (`ID`),
    ADD CONSTRAINT `suatchieu_ibfk_2` FOREIGN KEY (`ID_PhongChieu`) REFERENCES `phongchieu` (`ID`);

--
-- Constraints for table `taikhoan`
--
ALTER TABLE `taikhoan`
    ADD CONSTRAINT `taikhoan_ibfk_1` FOREIGN KEY (`ID_PhanLoaiTaiKhoan`) REFERENCES `phanloaitaikhoan` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
