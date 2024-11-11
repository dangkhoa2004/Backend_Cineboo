/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license/default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.math.*;
import java.time.*;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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

    @Size(min =1, max = 15)
    @Column(name = "MAVOUCHER")
    @NotBlank
    private String maVoucher ;

    @Range(min = 1)
    @Column(name = "GIATRIDOI")
    private Integer giaTriDoi;


    @Range(min=1,max=99)
    @Column(name = "TRUTIENPHANTRAM")
    private Integer truTienPhanTram;

    @DecimalMin(value = "1.0")
    @Digits(integer=18, fraction=2)
    @Column(name = "TRUTIENSO")
    private BigDecimal truTienSo;



    @DecimalMin(value = "0.0")
    @Digits(integer=18, fraction=2)
    @NotNull
    @Column(name = "SOTIENTOITHIEU")
    private BigDecimal soTienToiThieu;

    @DecimalMin(value = "1.0")
    @Digits(integer=18, fraction=2)
    @NotNull
    @Column(name = "GIAMTOIDA")
    private BigDecimal giamToiDa;

    @Column(name = "NGAYBATDAU")
    @NotNull
    private LocalDate ngayBatDau;

    @NotNull
    @Column(name = "NGAYKETTHUC")
    private LocalDate ngayKetThuc;

    @Column(name="SOLUONG")
    private Integer soLuong;

    @Column(name = "TRANGTHAIVOUCHER")
    private Integer trangThaiVoucher;
}
