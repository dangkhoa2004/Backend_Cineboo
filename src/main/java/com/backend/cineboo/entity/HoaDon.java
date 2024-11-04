/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.math.*;
import java.time.*;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 04dkh
 */
@Entity
@Table(name = "HOADON")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_KHACHHANG")
    @NotNull
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_PHIM")
    @NotNull
    private Phim phim;

    @ManyToOne
    @JoinColumn(name = "ID_VOUCHER")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "ID_PTTT")
    private PTTT pttt;

    //Avoid super duper looper hooper reflection
    @JsonIgnoreProperties("hoaDon")
    //Allow to manipulate child by manipulating the parent.
    //Deletion not allowed
    @OneToMany(mappedBy = "hoaDon"
            //,            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = false
    )
    List<ChiTietHoaDon> chiTietHoaDonList;

    @Column(name = "MAHOADON")
    @Size(max=15)
    private String maHoaDon;

    @Range(min = 0)
    @Column(name = "SOLUONG")
    private Integer soLuong;

    @NotNull
    @Column(name = "THOIGIANTHANHTOAN")
    private LocalDateTime thoiGianThanhToan;

    @Range(min=1)
    @Column(name = "DIEM")
    private Integer diem;

    @DecimalMin(value = "1.0")
    @Digits(integer=18, fraction=2)
    @Column(name = "TONGSOTIEN")
    private BigDecimal tongSoTien;

    @Range(min = 0)
    @Column(name = "TRANGTHAIHOADON")
    private Integer trangThaiHoaDon;
}
