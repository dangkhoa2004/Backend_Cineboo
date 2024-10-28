/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.time.*;
import lombok.*;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "NhanVien")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ChucVu")
    private PhanLoaiChucVu chucVu;

    @Column(nullable = false, length = 15)
    private String maNhanVien;

    private String ten;
    private String tenDem;
    private String ho;
    private LocalDate ngaySinh;
    private Integer gioiTinh;
    private String email;
    private String danToc;
    private String diaChi;
    private Integer trangThai;
}
