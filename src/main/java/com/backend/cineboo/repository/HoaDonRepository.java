package com.backend.cineboo.repository;

import com.backend.cineboo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HoaDonRepository extends JpaRepository<HoaDon,Long> {
    @Query(value = "SELECT MAX(ID) FROM HoaDon",nativeQuery = true)
    Long getMaxTableId();
}
