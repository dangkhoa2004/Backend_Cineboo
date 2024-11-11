package com.backend.cineboo.repository;

import com.backend.cineboo.entity.PTTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PTTTRepository extends JpaRepository<PTTT,Long> {
}
