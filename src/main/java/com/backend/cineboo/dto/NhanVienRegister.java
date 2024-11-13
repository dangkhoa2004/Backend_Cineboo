package com.backend.cineboo.dto;

import com.backend.cineboo.entity.PhanLoaiChucVu;
import com.backend.cineboo.entity.TaiKhoan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Data
@Getter
@Setter
public class NhanVienRegister {

    private Long id;


    private Long idChucVu;



    @NotBlank
    @Size(max = 100)
    private String ten;

    @NotBlank
    @Size(max = 10)
    private String tenDem;

    @NotBlank
    @Size(max = 50)
    private String ho;

    @NotNull
    private LocalDate ngaySinh;

    @NotNull
    private Integer gioiTinh;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String danToc;

    @NotBlank
    @Size(max = 255)
    private String diaChi;

}
