/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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
@Table(name = "Voucher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 15)
    private String maVoucher;

    private Integer giaTriDoi;
    private Integer truTienPhanTram;
    private BigDecimal truTienSo;
    private BigDecimal soTienGiam;
    private BigDecimal soTienToiThieu;
    private BigDecimal giamToiDa;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Integer trangThai;
}
