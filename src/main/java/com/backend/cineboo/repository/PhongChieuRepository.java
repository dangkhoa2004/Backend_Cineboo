package com.backend.cineboo.repository;

import com.backend.cineboo.entity.PhongChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhongChieuRepository extends JpaRepository<PhongChieu,Long> {
    @Query(value = "SELECT MAX(ID) FROM PhongChieu",nativeQuery = true)
    Long getMaxTableId();
}
