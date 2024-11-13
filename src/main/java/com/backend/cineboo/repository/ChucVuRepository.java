package com.backend.cineboo.repository;

import com.backend.cineboo.entity.PhanLoaiChucVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChucVuRepository extends JpaRepository<PhanLoaiChucVu,Long> {
}
