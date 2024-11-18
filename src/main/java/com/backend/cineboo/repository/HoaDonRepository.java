package com.backend.cineboo.repository;

import com.backend.cineboo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Long> {
    @Query(value = "SELECT MAX(ID) FROM HoaDon",nativeQuery = true)
    Long getMaxTableId();

    @Query(value="SELECT * FROM HoaDon  WHERE MaHoaDon = ? LIMIT 1 ",nativeQuery = true)
    Optional<HoaDon> findByMaHoaDon(String maHoaDon);
}
