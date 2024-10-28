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
@Table(name = "PhanLoaiChucVu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhanLoaiChucVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 15)
    private String maChucVu;

    @Column(nullable = false, length = 150)
    private String tenChucVu;

    private Integer trangThai;
}
