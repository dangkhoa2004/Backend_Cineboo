package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "KHACHHANG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PHANLOAI")
    private PhanLoaiKhachHang phanLoaiKhachHang;

    @Column(name = "TEN") // Specify the uppercase column name
    private String ten;

    @Column(name = "TENDEM") // Specify the uppercase column name
    private String tenDem;

    @Column(name = "HO") // Specify the uppercase column name
    private String ho;

    @Column(name = "NGAYSINH") // Specify the uppercase column name
    private LocalDate ngaySinh;

    @Column(name = "SODIENTHOAI") // Specify the uppercase column name
    private String soDienThoai;

    @Column(name = "GIOITINH") // Specify the uppercase column name
    private Integer gioiTinh;

    @Column(name = "EMAIL") // Specify the uppercase column name
    private String email;

    @Column(name = "TAIKHOAN") // Specify the uppercase column name
    private String taiKhoan;

    @Column(name = "MATKHAU") // Specify the uppercase column name
    private String matKhau;

    @Column(name = "DANTOC") // Specify the uppercase column name
    private String danToc;

    @Column(name = "DIACHI") // Specify the uppercase column name
    private String diaChi;

    @Column(name = "DIEM") // Specify the uppercase column name
    private Integer diem;

    @Column(name = "TRANGTHAI") // Specify the uppercase column name
    private Integer trangThaiKhachHang;
}
