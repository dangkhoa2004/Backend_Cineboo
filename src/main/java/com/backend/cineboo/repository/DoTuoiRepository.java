package com.backend.cineboo.repository;

import com.backend.cineboo.entity.DoTuoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface DoTuoiRepository  extends JpaRepository<DoTuoi,Long> {
    @Query(value = "SELECT MAX(ID) FROM DoTuoi",nativeQuery = true)
    Long getMaxTableId();

    @Query(value = "SELECT * FROM DoTuoi WHERE TenDoTuoi = ? ",nativeQuery = true)
    Optional<DoTuoi> checkDuplicate(String tenDoTuoi);
}
