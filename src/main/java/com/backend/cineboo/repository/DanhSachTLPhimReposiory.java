package com.backend.cineboo.repository;

import com.backend.cineboo.entity.DanhSachTLPhim;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhSachTLPhimReposiory extends JpaRepository<DanhSachTLPhim,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DanhSachTLPhim WHERE ID_Phim = ?",nativeQuery = true)
    void deleteAllByID_Phim(String id_Phim);
}
