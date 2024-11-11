package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PHANLOAITAIKHOAN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhanLoaiTaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TENLOAITAIKHOAN", nullable = false, unique = true)
    private String tenLoaiTaiKhoan;

    @Column(name = "TRANGTHAIPHANLOAITAIKHOAN")
    private Integer trangThaiPhanLoaiTaiKhoan;
}
