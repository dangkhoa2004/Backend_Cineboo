package com.backend.cineboo.repository;

import com.backend.cineboo.entity.TheLoaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheLoaiPhimRepository extends JpaRepository<TheLoaiPhim,Long> {
    @Query(value = "SELECT MAX(ID) FROM TheLoaiPhim",nativeQuery = true)
    Long getMaxTableId();

    @Query(value = "SELECT * FROM TheLoaiPhim  Where TenTheLoai = ? "
            , nativeQuery = true)
    Optional<TheLoaiPhim> checkDuplicate(String tenTheLoai);
}
