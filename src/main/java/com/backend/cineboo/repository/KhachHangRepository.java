package com.backend.cineboo.repository;

import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {
    @Query(value="SELECT * FROM KhachHang WHERE ID_TaiKhoan = ? LIMIT 1",nativeQuery = true)
    Optional<KhachHang> findByID_TaiKhoan(String id_TaiKhoan);

    @Query(value = "SELECT MAX(ID) FROM KhachHang",nativeQuery = true)
    Long getMaxTableId();
}
