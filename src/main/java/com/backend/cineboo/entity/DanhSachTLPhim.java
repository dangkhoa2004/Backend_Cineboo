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
@Table(name = "DanhSachTLPhim")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DanhSachTLPhim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Phim")
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "ID_TLPhim")
    private TheLoaiPhim theLoaiPhim;

    private Integer trangThai;
}
