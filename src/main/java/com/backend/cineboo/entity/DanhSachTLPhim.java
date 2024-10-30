/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "DANHSACHTLPHIM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DanhSachTLPhim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_PHIM")
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "ID_TLPHIM")
    private TheLoaiPhim theLoaiPhim;

    @Column(name = "TRANGTHAI")
    private Integer trangThai;


}
