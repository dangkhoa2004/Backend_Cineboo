package com.backend.cineboo.repository;

import com.backend.cineboo.entity.PTTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PTTTRepository extends JpaRepository<PTTT,Long> {
    @Query(value = "SELECT MAX(ID) FROM PTTT",nativeQuery = true)
    Long getMaxTableId();

    @Query(value = "SELECT * FROM PTTT WHERE TenPTTT = ? ",nativeQuery=true)
    Optional<PTTT> checkDuplicate(String tenPTTT);
}
