package com.backend.cineboo.repository;

import com.backend.cineboo.dto.GheWithoutSuatChieuId;
import com.backend.cineboo.entity.Ghe;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GheRepository extends JpaRepository<Ghe,Long> {
    @Query(value = "SELECT " +
            "g.ID AS GheID, " +
            "g.MaGhe, " +
            "g.GiaTien, " +
            "g.TrangThaiGhe AS TrangThaiGhe, " +
            "CASE " +
            "    WHEN s.ID IS NOT NULL THEN 'Đã đặt' " +
            "    ELSE 'Chưa đặt' " +
            "END AS GheTrangThai " +
            "FROM " +
            "Ghe g " +
            "JOIN SuatChieu s ON g.ID_PhongChieu = s.ID_PhongChieu " +
            "AND s.ThoiGianChieu = ?2 " +
            "WHERE " +
            "s.ID_Phim = ?1 " +
            "ORDER BY " +
            "g.MaGhe", nativeQuery = true)
    List<GheWithoutSuatChieuId> findByIdPhimAndThoiGianChieu(String idPhim, String thoiGianChieu);

    @Query(value="SELECT * FROM Ghe WHERE ID_PhongChieu = ?",nativeQuery = true)
    List<Ghe> findByID_PhongChieu(String id_PhongChieu);
}
