package com.backend.cineboo.repository;

import com.backend.cineboo.entity.DanhSachTLPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhSachTLPhimReposiory extends JpaRepository<DanhSachTLPhim,Long> {
}
