package com.backend.cineboo.repository;

import com.backend.cineboo.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    @Query(name = "SELECT ID, TenDangNhap, MatKhau, ID_PhanLoaiTaiKHoan, TrangThaiTaiKhoan FROM TaiKhoan WHERE TenTaiKhoan = ? ", nativeQuery = true)
    Optional<TaiKhoan> findByTenDangNhap(String tenTaiKhoan);

    @Query(value = "SELECT 1 FROM TaiKhoan WHERE TenDangNhap = ? LIMIT 1", nativeQuery = true)
    Optional<Integer> checkAvailable(String tenDangNhap);

    @Query(value = "SELECT  (TenPhanLoaiKhachHang) FROM taikhoan" +
            " JOIN khachhang  ON taikhoan.ID = khachhang.ID_TaiKhoan " +
            " JOIN PhanloaiKhachhang ON khachhang.ID_PhanLoai = phanloaikhachhang.ID " +
            " WHERE taikhoan.ID = ? ", nativeQuery = true)
    Optional<String> getPhanLoaiKhachHang(String idTaiKhoan);

    @Query(value = "SELECT (TenChucVu) " +
            "FROM taikhoan " +
            "         JOIN nhanvien n on taikhoan.ID = n.ID_TaiKhoan " +
            "         JOIN phanloaichucvu p on p.ID = n.ID_ChucVu " +
            "WHERE taikhoan.ID = ? ", nativeQuery = true)
    Optional<String> getPhanLoaiNhanVien(String idTaiKhoan);



}
