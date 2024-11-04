/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.math.*;
import lombok.*;
/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "GHE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ghe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="MAGHE")
    private String maGhe;

    @Column(name="GIATIEN")
    private BigDecimal giaTien;

    @ManyToOne
    @JoinColumn(name = "ID_PHONGCHIEU")
    private PhongChieu phongChieu;

    @Column(name="TRANGTHAIGHE")
    private Integer trangThaiGhe;
}
