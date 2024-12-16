package com.backend.cineboo.dto;

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
public class KhachHangRegister {




    @NotBlank
    @Size(max = 15)
    private String ten;

    @Size(max = 50)
    private String tenDem;

    @NotBlank
    @Size(max = 50)
    private String ho;

    @NotNull
    private LocalDate ngaySinh;

    @NotBlank
    @Size(min = 10, max = 10)
    private String soDienThoai;

    @NotNull
    private Integer gioiTinh;

    @NotBlank
    @Email
    @Size(min=6, max = 255)
    private String email;

    @Size(max = 75)
    private String danToc ;

    @NotBlank
    @Size(max = 255)
    private String diaChi;


}
