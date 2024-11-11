package com.backend.cineboo.repository;

import com.backend.cineboo.entity.KhoQua;
import com.backend.cineboo.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhoQuaReposiory extends JpaRepository<KhoQua,Long> {
}
