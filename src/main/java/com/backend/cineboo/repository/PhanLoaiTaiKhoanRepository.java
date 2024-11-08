package com.backend.cineboo.repository;

import com.backend.cineboo.entity.PhanLoaiTaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanLoaiTaiKhoanRepository extends JpaRepository<PhanLoaiTaiKhoan,Long> {
}
