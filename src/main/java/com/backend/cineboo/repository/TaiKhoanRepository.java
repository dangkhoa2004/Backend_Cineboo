package com.backend.cineboo.repository;

import com.backend.cineboo.entity.TaiKhoan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    @Query(value = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ? ", nativeQuery = true)
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



    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update taikhoan set otp = ? where email = ? ",nativeQuery = true)
    Integer updateOtpByEmail(String otp, String email);

    @Query(value = "SELECT OTP FROM TaiKhoan WHERE Email = ?",nativeQuery = true)
    Optional<String> getOTPByEmail(String email);

    @Query(value = "SELECT * FROM TaiKhoan WHERE Email = ? LIMIT 1",nativeQuery = true)
    Optional<TaiKhoan> findByEmail(String email);

    @Query(value = "SELECT EXISTS(SELECT * FROM taikhoan WHERE Email = ? AND (OTP IS NOT NULL AND TRIM(OTP) != '') LIMIT 1)",nativeQuery = true)
    Integer checkIfOTPExistsByEmail(String email, String blankChar);
}
