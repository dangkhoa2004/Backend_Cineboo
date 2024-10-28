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
@Table(name = "KhachHang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PhanLoai")
    private PhanLoaiKhachHang phanLoaiKhachHang;

    private String ten;
    private String tenDem;
    private String ho;
    private LocalDate ngaySinh;
    private String soDienThoai;
    private Integer gioiTinh;
    private String email;
    private String danToc;
    private String diaChi;
    private Integer diem;
    private Integer trangThai;
}
