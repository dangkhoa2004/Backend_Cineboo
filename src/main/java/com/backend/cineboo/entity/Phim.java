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
@Table(name = "Phim")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 15)
    private String maPhim;

    @Column(nullable = false, length = 255)
    private String tenPhim;

    @Column(nullable = false, length = 255)
    private String anhPhim;

    @Column(nullable = false, length = 255)
    private String dienVien;

    @Column(nullable = false, length = 200)
    private String theLoai;

    private Integer nam;
    private String noiDungMoTa;
    private String trailer;
    private Integer thoiLuong;
    private String quocGia;
    private String noiDung;
    private Integer gioiHanDoTuoi;
    private Integer trangThai;
}
