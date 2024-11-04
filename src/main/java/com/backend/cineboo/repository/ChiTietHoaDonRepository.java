package com.backend.cineboo.repository;

import com.backend.cineboo.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Long> {
    @Query(value=" SELECT DISTINCT SUM(g.GiaTien) " +
            " FROM chitiethoadon JOIN ghe g on g.ID = chitiethoadon.ID_Ghe " +
            " WHERE chitiethoadon.ID= ? ",nativeQuery = true)
    Optional<BigDecimal> getFinalPrice(Long chiTietHoaDonId);
}
