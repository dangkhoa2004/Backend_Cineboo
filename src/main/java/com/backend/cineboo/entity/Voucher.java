/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license/default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.math.*;
import java.time.*;
import lombok.*;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "VOUCHER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MAVOUCHER")
    private String maVoucher;

    @Column(name = "GIATRIDOI")
    private Integer giaTriDoi;

    @Column(name = "TRUTIENPHANTRAM")
    private Integer truTienPhanTram;

    @Column(name = "TRUTIENSO")
    private BigDecimal truTienSo;

    @Column(name = "SOTIENGIAM")
    private BigDecimal soTienGiam;

    @Column(name = "SOTIENTOITHIEU")
    private BigDecimal soTienToiThieu;

    @Column(name = "GIAMTOIDA")
    private BigDecimal giamToiDa;

    @Column(name = "NGAYBATDAU")
    private LocalDate ngayBatDau;

    @Column(name = "NGAYKETTHUC")
    private LocalDate ngayKetThuc;

    @Column(name = "TRANGTHAIVOUCHER")
    private Integer trangThaiVoucher;
}
