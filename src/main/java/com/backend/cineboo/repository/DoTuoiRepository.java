package com.backend.cineboo.repository;

import com.backend.cineboo.entity.DoTuoi;
import com.backend.cineboo.entity.PTTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoTuoiRepository  extends JpaRepository<DoTuoi,Long> {
    @Query(value = "SELECT MAX(ID) FROM Phim",nativeQuery = true)
    Long getMaxTableId();

    @Query(value = "SELECT * FROM DoTuoi WHERE TenDoTuoi = ? ",nativeQuery=true)
    Optional<DoTuoi> checkDuplicate(String tenDoTuoi);
}
