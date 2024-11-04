/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "PHONGCHIEU")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhongChieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="MAPHONG")
    private String maPhong;

    @Column(name="TONGSOGHE")
    private Integer tongSoGhe;

    @Column(name="TRANGTHAIPHONGCHIEU")
    private Integer trangThaiPhongChieu;
}
