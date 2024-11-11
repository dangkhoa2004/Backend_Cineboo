package com.backend.cineboo.repository;

import com.backend.cineboo.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query(value = "SELECT * FROM Voucher WHERE MaVoucher = ? LIMIT 1", nativeQuery = true)
    Optional<Voucher> findByMaVoucher(String maVoucher);

    @Query(value = "SELECT * FROM Voucher WHERE MaVoucher = ? "
            + " AND TrangThaiVoucher = 1   AND SoLuong > 0 "
            + "  AND (NgayBatDau IS NULL OR NgayBatDau <= CURDATE())"
            + " AND (NgayKetThuc IS NULL OR NgayKetThuc >= CURDATE()) LIMIT 1"
            , nativeQuery = true)
    Optional<Voucher> checkAvailabilityByMaVoucher(String maVoucher);

    @Query(value = "SELECT v.* FROM khoqua kq\n" +
            "    JOIN voucher v on v.ID = kq.ID_Voucher\n" +
            "    JOIN khachhang k on k.ID = kq.ID_KhachHang\n" +
            "    WHERE ID_KhachHang = ? ",nativeQuery = true)
    List<Voucher> findByID_KhachHang(Long id_KhachHang);


}
