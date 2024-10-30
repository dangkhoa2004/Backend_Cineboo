package com.backend.cineboo.repository;

import com.backend.cineboo.entity.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SuatChieuRepository extends JpaRepository<SuatChieu,Long> {
    @Query(value = "SELECT MAX(ID) FROM SuatChieu",nativeQuery = true)
    Long getMaxTableId();
}
