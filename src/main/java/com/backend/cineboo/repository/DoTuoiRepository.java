package com.backend.cineboo.repository;

import com.backend.cineboo.entity.DoTuoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoTuoiRepository  extends JpaRepository<DoTuoi,Long> {
}
