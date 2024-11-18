/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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

    @Column(name="MAPHONG",length = 15)
    @Size(max = 15)
    private String maPhong;

    @Range(min = 0)
    @Column(name="TONGSOGHE")
    private Integer tongSoGhe;

    @Range(min = 0)
    @Column(name="TRANGTHAIPHONGCHIEU")
    private Integer trangThaiPhongChieu;
}
