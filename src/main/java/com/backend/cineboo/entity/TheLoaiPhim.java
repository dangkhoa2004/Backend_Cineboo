/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
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
@Table(name = "THELOAIPHIM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheLoaiPhim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MATLPHIM",length = 15)
    @NotBlank
    @Size(max=15)
    private String maTLPhim;

    @Column(name = "TENTHELOAI",length = 100)
    @NotBlank
    @Size(max=100)
    private String tenTheLoai;

    @NotNull
    @Range(min = 0)
    @Column(name = "TRANGTHAI")
    private Integer trangThai;
}
