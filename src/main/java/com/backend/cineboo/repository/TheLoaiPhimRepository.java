package com.backend.cineboo.repository;

import com.backend.cineboo.entity.TheLoaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheLoaiPhimRepository extends JpaRepository<TheLoaiPhim,Long> {
}
