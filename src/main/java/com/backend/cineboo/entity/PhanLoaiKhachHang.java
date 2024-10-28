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
@Table(name = "PhanLoaiKhachHang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhanLoaiKhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 15)
    private String maKhachHang;

    @Column(nullable = false, length = 100)
    private String loaiKhachHang;

    private Integer trangThai;
}
