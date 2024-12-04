/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "CHITIETHOADON")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_HOADON")
    @NotNull
    private HoaDon hoaDon;

    @JoinColumn(name = "ID_GHEANDSUATCHIEU")
    @OneToOne
    @NotNull
    private GheAndSuatChieu id_GheAndSuatChieu;

    @Range(min = 0)
    @Column(name ="TRANGTHAICHITIETHOADON")
    private Integer trangThaiChiTietHoaDon;
}
