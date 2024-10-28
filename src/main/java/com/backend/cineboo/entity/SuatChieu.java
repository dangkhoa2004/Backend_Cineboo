/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.time.*;
import lombok.*;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "PhanLoaiKhachHang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuatChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 15)
    private String maSuatChieu;

    private LocalDateTime thoiGianChieu;

    @ManyToOne
    @JoinColumn(name = "ID_Phim")
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "ID_PhongChieu")
    private PhongChieu phongChieu;

    private Integer trangThai;
}
