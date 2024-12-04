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
    
    @Query(value="select g2.ID_PhongChieu from hoadon " +
            "         join chitiethoadon c on hoadon.ID = c.ID_HoaDon " +
            "         join gheandsuatchieu g on g.ID = c.ID_GheAndSuatChieu " +
            "         join ghe g2 on g.ID_GHE = g2.ID " +
            "where hoadon.id= ?  LIMIT 1", nativeQuery = true)
    Integer getIDPhongChieuByHoaDonId(String id_HoaDon);


}
