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
@Table(name = "SUATCHIEU")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuatChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="MASUATCHIEU")
    private String maSuatChieu;

    @Column(name="THOIGIANCHIEU")
    private LocalDateTime thoiGianChieu;

    @ManyToOne
    @JoinColumn(name = "ID_PHIM")
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "ID_PHONGCHIEU")
    private PhongChieu phongChieu;

    @Column(name="TRANGTHAI")
    private Integer trangThai;
}
