package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TAIKHOAN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MATAIKHOAN", insertable = false, updatable = false)
    private String maTaiKhoan;

    @Column(name = "TENDANGNHAP", nullable = false, unique = true)
    private String tenDangNhap;

    @Column(name = "MATKHAU", nullable = false)
    private String matKhau;

    @Column(name = "TRANGTHAITAIKHOAN", nullable = false)
    private Integer trangThaiTaiKhoan;

    @ManyToOne
    @JoinColumn(name = "ID_PHANLOAITAIKHOAN", nullable = false)
    private PhanLoaiTaiKhoan phanLoaiTaiKhoan;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "GHICHU")
    private String ghiChu;

    @Column(name="EMAIL",nullable = false)
    @NotBlank
    @Size(min=6,max = 255 )
    @Email
    private String email;
}
