/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.math.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "GHE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ghe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="MAGHE",length = 15)
    @NotBlank
    @Size(max = 15)
    private String maGhe;

    @Column(name="GIATIEN")
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal giaTien;

    @ManyToOne
    @JoinColumn(name = "ID_PHONGCHIEU")
    @NotNull
    private PhongChieu phongChieu;

    @NotNull
    @Range(min = 0)
    @Column(name="TRANGTHAIGHE")
    private Integer trangThaiGhe;
}
