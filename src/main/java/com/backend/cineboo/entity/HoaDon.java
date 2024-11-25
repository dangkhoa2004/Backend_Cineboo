/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private Long id ;

    @ManyToOne
    @JoinColumn(name = "ID_KHACHHANG")
    @NotNull
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_SUATCHIEU")
    @NotNull
    private SuatChieu suatChieu;

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

    @Range(min=0)
    @Column(name = "DIEM")
    private Integer diem;

    //If value is not null, it must adhere to these checks below
    @DecimalMin(value = "0.0")
//    @Digits(integer=18, fraction=2)
//    @NotNull/ DO NOT UNCOMMENT THIS IF YOU WANT TO BE ABLE TO MAKE A BLANK INVOICE
    @Column(name = "TONGSOTIEN")
    private BigDecimal tongSoTien;

    @Range(min = 0)
    @Column(name = "TRANGTHAIHOADON")
    private Integer trangThaiHoaDon;
}
