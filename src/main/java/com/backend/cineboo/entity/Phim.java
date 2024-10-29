/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "Phim")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Trường dữ liệu nên được tự động khởi tạo thay vì nhập tay
    @Column(name = "MAPHIM")
    private String maPhim;

    @NotBlank(message = "Thiếu tên phim")
    @Column(name = "TENPHIM")
    private String tenPhim;

    @NotBlank(message = "Thiếu ảnh phim")
    @Column(name = "ANHPHIM")
    private String anhPhim;

    @NotBlank(message = "Thiếu diễn viên")
    @Column(name = "DIENVIEN")
    private String dienVien;

    @OneToMany(mappedBy = "phim")
    private List<DanhSachTLPhim> danhSachTLPhims;


    @Range(min = 1888, max = 2999,message = "Năm sản xuất phải từ 1888-2999")
    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "NOIDUNGMOTA")
    private String noiDungMoTa;

    @Column(name = "TRAILER")
    private String trailer;

    @NotNull(message = "Thiếu Ngày ra mắt")
    @Column(name = "NGAYRAMAT")
    private Date ngayRaMat;

    @Range(min =1,message = "Thời lượng phải lớn hơn 1")
    @Column(name = "THOILUONG")
    private Integer thoiLuong;

    @NotBlank(message = "Thiếu Quốc gia")
    @Column(name = "QUOCGIA")
    private String quocGia;

    @Column(name = "NOIDUNG")
    private String noiDung;

    @NotNull(message = "Thiếu giới hạn độ tuổi")
    @ManyToOne
    @JoinColumn(name = "GIOIHANDOTUOI")
    private DoTuoi gioiHanDoTuoi;

    @Column(name = "TRANGTHAI")
    private Integer trangThai;
}
