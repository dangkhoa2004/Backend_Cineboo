package com.backend.cineboo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @JsonIgnoreProperties("matKhau")
    @OneToOne
    @JoinColumn(name = "ID_TAIKHOAN")
    private TaiKhoan taiKhoan;

    @NotBlank
    @Size(max = 15)
    @Column(name = "MAKHACHHANG", nullable = false)
    private String maKhachHang;

    @NotBlank
    @Size(max = 15)
    @Column(name = "TEN", nullable = false)
    private String ten;

    @Size(max = 50)
    @Column(name = "TENDEM")
    private String tenDem;

    @NotBlank
    @Size(max = 50)
    @Column(name = "HO", nullable = false)
    private String ho;

    @NotNull
    @Column(name = "NGAYSINH", nullable = false)
    private LocalDate ngaySinh;

    @NotBlank
    @Size(min = 10, max = 10)
    @Column(name = "SODIENTHOAI", nullable = false)
    private String soDienThoai;

    @NotNull
    @Column(name = "GIOITINH", nullable = false)
    private Integer gioiTinh;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Size(max = 75)
    @Column(name = "DANTOC")
    private String danToc;

    @NotBlank
    @Size(max = 255)
    @Column(name = "DIACHI", nullable = false)
    private String diaChi;

    @Column(name = "DIEM")
    private Integer diem;

    @Column(name = "TRANGTHAIKHACHHANG")
    private Integer trangThaiKhachHang;
}
