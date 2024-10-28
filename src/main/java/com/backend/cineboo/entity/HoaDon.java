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
@Table(name = "Ghe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_KhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_Phim")
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "ID_Voucher")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "ID_PTTT")
    private PTTT pttt;

    private String maHoaDon;
    private Integer soLuong;
    private LocalDateTime thoiGianThanhToan;
    private Integer diem;
    private BigDecimal tongSoTien;
    private Integer trangThai;
}
