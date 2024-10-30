package com.backend.cineboo.repository;

import com.backend.cineboo.entity.Phim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhimRepository extends JpaRepository<Phim,Long> {
    @Query(value = "SELECT MAX(ID) FROM Phim",nativeQuery = true)
     Long getMaxTableId();
}
