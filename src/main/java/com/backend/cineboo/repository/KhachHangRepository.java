package com.backend.cineboo.repository;

import com.backend.cineboo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {
    @Query(name = "SELECT TaiKhoan, MatKhau FROM KhachHang WHERE TAIKHOAN=?", nativeQuery = true)
     Optional<KhachHang> findByTaiKhoan(String taiKhoan);
}
