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
@Table(name = "THELOAIPHIM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheLoaiPhim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MATLPHIM")
    private String maTLPhim;

    @Column(name = "TENTHELOAI")
    private String tenTheLoai;

    @Column(name = "TRANGTHAI")
    private Integer trangThai;
}
