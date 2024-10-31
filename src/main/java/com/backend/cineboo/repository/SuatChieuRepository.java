package com.backend.cineboo.repository;

import com.backend.cineboo.entity.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuatChieuRepository extends JpaRepository<SuatChieu,Long> {
    @Query(value = "SELECT MAX(ID) FROM SuatChieu",nativeQuery = true)
    Long getMaxTableId();

    Optional<SuatChieu> findByMaSuatChieu(String maSuatChieu);

    @Query(value = "SELECT * FROM SuatChieu  WHERE ID_Phim = ?", nativeQuery = true)
    List<SuatChieu> findByID_Phim(String id_Phim);

    @Query(value = "SELECT * FROM suatchieu WHERE Date(ThoiGianChieu)=?",nativeQuery = true)
    List<SuatChieu> findByThoiGianChieu(String thoiGianChieu);
}
