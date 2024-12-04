package com.backend.cineboo.repository;

import com.backend.cineboo.entity.GheAndSuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GheAndSuatChieuRepository extends JpaRepository<GheAndSuatChieu, Long> {

    @Query(value = "SELECT * FROM GheAndSuatChieu WHERE ID_Ghe = ?1 AND ID_SuatChieu = ?2 ", nativeQuery = true)
    Optional<GheAndSuatChieu> findByGheAndSuatChieu(String id_Ghe, String id_SuatChieu);

    @Query(value = "SELECT * FROM GheAndSuatChieu WHERE  ID_SuatChieu = ? ", nativeQuery = true)
    List<GheAndSuatChieu> findByID_SuatChieu(String id_SuatChieu);

    @Query(value = "SELECT 1 " +
            "FROM gheandsuatchieu " +
            "         JOIN suatchieu s ON gheandsuatchieu.ID_SUATCHIEU = s.ID " +
            "         JOIN ghe g ON g.ID = gheandsuatchieu.ID_GHE " +
            "WHERE s.ID_Phim = ?1 " +
            "  AND g.ID_PhongChieu = ?2 " +
            "  AND s.ThoiGianChieu = ?3 " +
            "LIMIT 1 ", nativeQuery = true)
     Integer checkSuatChieuDuplicate(String id_Phim,String id_PhongChieu,String thoiGianChieu);
}
