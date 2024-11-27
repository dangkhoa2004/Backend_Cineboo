package com.backend.cineboo.repository;

import com.backend.cineboo.entity.DanhSachHoanVe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhSachHoanVeRepository extends JpaRepository< DanhSachHoanVe,Long> {

    //Database use Unique constraint, but check duplicate anyway
    @Query(value = "SELECT EXISTS(SELECT * FROM DanhSachHoanVe  WHERE ID_HoaDon = ?  LIMIT 1)",nativeQuery = true)
    Integer checkDuplicate(String id_HoaDon);
}
