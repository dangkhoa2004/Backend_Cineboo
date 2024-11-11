/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "KHOQUA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhoQua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_KHACHHANG")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_VOUCHER")
    private Voucher voucher;
}
