package com.backend.cineboo.repository;

import com.backend.cineboo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien,Long> {

    @Query(value="SELECT * FROM NhanVien WHERE ID_TaiKhoan = ? LIMIT 1",nativeQuery = true)
    Optional<NhanVien> findByID_TaiKhoan(Long id_TaiKhoan);
}
