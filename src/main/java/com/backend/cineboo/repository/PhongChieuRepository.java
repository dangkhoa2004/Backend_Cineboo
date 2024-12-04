package com.backend.cineboo.repository;

import com.backend.cineboo.entity.PhongChieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface PhongChieuRepository extends JpaRepository<PhongChieu,Long> {
    @Query(value = "SELECT MAX(ID) FROM PhongChieu",nativeQuery = true)
    Long getMaxTableId() ;
	
	@Query(value = "SELECT  phongchieu.id, phongchieu.maPhong,phongchieu.TongSoGhe,phongchieu.TrangThaiPhongChieu FROM phongchieu join ghe ON ghe.ID_PhongChieu = phongchieu.ID JOIN gheandsuatchieu on gheandsuatchieu.ID_GHE= ghe.ID where gheandsuatchieu.ID_SUATCHIEU= ? LIMIT 1",nativeQuery=true)
	Optional<PhongChieu> getPhongChieuByID_SuatChieu(String id_SuatChieu);
}
