/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 04dkh
 */
@Entity
@Table(name = "PHANLOAIKHACHHANG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhanLoaiKhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 15, name = "MAKHACHHANG")
    private String maKhachHang;

    @Column(nullable = false, length = 100, name = "TENPHANLOAIKHACHHANG")
    private String tenPhanLoaiKhachHang;


    //Sử dụng Transient để tạm thời bỏ qua thuộc tính này
    //Lý do: trùng lặp tên cột PhanLoaiKhachHang và KhachHang, 2 bảng đều có cột tên là TrangThai
    @Column(name = "TRANGTHAI")
    @Transient
    private Integer trangThaiPhanLoaiKhachHang;
}
