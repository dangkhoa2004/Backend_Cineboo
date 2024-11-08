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
@Table(name = "PHANLOAICHUCVU")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhanLoaiChucVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 15,name="MACHUCVU")
    private String maChucVu;

    @Column(nullable = false, length = 150, name="TENCHUCVU")
    private String tenChucVu;

    @Column(name="TRANGTHAIPHANLOAICHUCVU")
    private Integer trangThaiPhanLoaiChucVu;
}
