package com.backend.cineboo.repository;

import com.backend.cineboo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query(value = "SELECT EXISTS(SELECT * FROM Review  WHERE ID_KhachHang = ?1 AND ID_Phim= ?2  LIMIT 1)",nativeQuery = true)
    Integer checkDuplicate(String id_KhachHang,String id_Phim);

    @Query(value = "SELECT EXISTS(SELECT * FROM khachhang\n" +
            "    JOIN hoadon h on khachhang.ID = h.ID_KhachHang\n" +
            "    JOIN suatchieu s on h.ID_SuatChieu = s.ID\n" +
            "    JOIN phim p on s.ID_Phim = p.ID\n" +
            "    WHERE khachhang.ID= ? \n" +
            "    LIMIT 1)",nativeQuery = true)
    Integer checkIfKhachHangHadViewedMovie(String id_KhachHang);

    @Query(value = "SELECT AVG(DanhGia) FROM Review where ID_Phim = ? ",nativeQuery = true)
    BigDecimal averageRating(String id_Phim);

    List<Review> findAllByTrangThaiReview(Integer trangThaiReview);
}
