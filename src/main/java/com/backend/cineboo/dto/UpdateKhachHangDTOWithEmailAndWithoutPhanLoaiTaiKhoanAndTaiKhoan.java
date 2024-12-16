package com.backend.cineboo.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
@Data
@Getter
@Setter
public class UpdateKhachHangDTOWithEmailAndWithoutPhanLoaiTaiKhoanAndTaiKhoan {

    @NotNull
    @Range(min=1, message = "id phai lon hon hoac bang 1")
    private Long id;

    @NotBlank
    @Size(max = 15, message = "Ten toi da 15 ky tu")
    private String ten;

    @Size(max = 50,message = "Ten toi da 50 ky tu")
    private String tenDem;

    @NotBlank
    @Size(max = 50,message = "Ten toi da 50 ky tu")
    private String ho;

    @NotNull
    private LocalDate ngaySinh;

    @NotBlank
    @Size(min = 10, max = 10,message = "soDienThoai 10 ky tu")
    private String soDienThoai;

    @NotNull
    @Range(min=0,message = "Gioi tinh phai lon hon hoac bang 0")
    private Integer gioiTinh;



    @Size(max = 75,message = "danToc toi da 75 ky tu")
    @Column(name = "DANTOC")
    private String danToc;

    @NotBlank
    @Size(max = 255,message = "diaChi toi da 255 ky tu")
    private String diaChi;

    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

}
