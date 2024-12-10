package com.backend.cineboo.repository;

import com.backend.cineboo.dto.GheWithoutSuatChieuId;

import com.backend.cineboo.entity.Ghe;
import com.backend.cineboo.entity.GheWithBookingStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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

    @Query(value ="SELECT g.ID,g.MaGhe,g.GiaTien,g.ID_PhongChieu,g.TrangThaiGhe " +
            "            FROM gheandsuatchieu " +
            "JOIN ghe g on gheandsuatchieu.ID_GHE = g.ID " +
            "where ID_SUATCHIEU= ?;",nativeQuery = true)
    List<Ghe> findByID_SuatChieu(String id_suatChieu);

    @Query(value = "SELECT MAX(ID) FROM Ghe",nativeQuery = true)
    Long getMaxTableId();

    @Query(value = "SELECT * FROM GHE WHERE ID_PhongChieu= ?1 AND MaGhe= ?2 "
            ,nativeQuery = true)
    Optional<Ghe> findByID_PhongChieuAndMaGhe(String id_PhongChieu, String maGhe);
	
	



      @Query(value = "SELECT EXISTS(select * FROM ghe JOIN phongchieu on ghe.ID_PhongChieu=phongchieu.ID WHERE phongchieu.ID= ? LIMIT 1)"
            ,nativeQuery = true)
    Integer checkIfGheOfPhongChieuExists(String id_suatChieu);


	
}
