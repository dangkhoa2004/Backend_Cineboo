package com.backend.cineboo.dto;

import com.backend.cineboo.entity.KhachHang;
import com.backend.cineboo.entity.NhanVien;
import com.backend.cineboo.entity.PhanLoaiTaiKhoan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TaiKhoanWithNhanVien {

    private Long id;

    private String maTaiKhoan;

    private String tenDangNhap;

    private String matKhau;

    private Integer trangThaiTaiKhoan;

    private PhanLoaiTaiKhoan phanLoaiTaiKhoan;


    private String otp;


    private String ghiChu;


    private String email;

    @JsonIgnoreProperties("taiKhoan")
    private NhanVien nhanVien;
}
