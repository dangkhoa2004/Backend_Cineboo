package com.backend.cineboo.repository;

import com.backend.cineboo.dto.GheWithoutSuatChieuId;
import com.backend.cineboo.entity.Ghe;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query(value ="SELECT Ghe.ID,Ghe.MaGhe,Ghe.GiaTien,Ghe.ID_PhongChieu,Ghe.TrangThaiGhe " +
            "FROM `ghe`" +
            "JOIN `phongchieu` ON `ghe`.`ID_PhongChieu` = `phongchieu`.`ID`" +
            "JOIN `suatchieu` ON `phongchieu`.`ID` = `suatchieu`.`ID_PhongChieu`" +
            "WHERE `suatchieu`.`ID` = ?;",nativeQuery = true)
    List<Ghe> findByID_SuatChieu(String id_suatChieu);
}
