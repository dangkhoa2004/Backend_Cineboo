package com.backend.cineboo.repository;

import com.backend.cineboo.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query(value = "SELECT * FROM Voucher WHERE MaVoucher = ? LIMIT 1", nativeQuery = true)
    Optional<Voucher> findByMaVoucher(String maVoucher);

    @Query(value = "SELECT * FROM Voucher WHERE MaVoucher = ? "
            + " AND TrangThaiVoucher = 1   AND SoLuong > 0 "
            + "  AND (NgayBatDau IS NULL OR NgayBatDau <= CURDATE())"
            + " AND (NgayKetThuc IS NULL OR NgayKetThuc >= CURDATE()) LIMIT 1"
            , nativeQuery = true)
    Optional<Voucher> checkAvailabilityByMaVoucher(Long maVoucher);


}
