package com.backend.cineboo.repository;

import com.backend.cineboo.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Long> {
    @Query(value=" SELECT DISTINCT SUM(g.GiaTien) " +
            " FROM chitiethoadon JOIN ghe g on g.ID = chitiethoadon.ID_Ghe " +
            " WHERE chitiethoadon.ID_HoaDon = ? ",nativeQuery = true)
    Optional<BigDecimal> getFinalPrice(Long id_HoaDon);

    @Query(value = "SELECT * FROM ChiTietHoaDon " +
            "WHERE ID_HoaDon = ?1 AND ID_Ghe = ?2 " +
            "AND TrangThaiChiTietHoaDon = 0 ",nativeQuery = true)
    Optional<ChiTietHoaDon> checkDuplicate(Long ID_HoaDon, Long ID_Ghe );

    @Query(value = "SELECT * FROM ChiTietHoaDon  WHERE  ID_HoaDon = ? ",nativeQuery = true)
    List<ChiTietHoaDon> getChiTietHoaDonsByID_HoaDon(String id_HoaDon);
}
