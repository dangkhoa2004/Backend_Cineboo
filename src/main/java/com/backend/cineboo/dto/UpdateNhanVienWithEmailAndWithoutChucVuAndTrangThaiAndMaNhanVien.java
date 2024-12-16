package com.backend.cineboo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
@Getter
@Setter
@Data
public class UpdateNhanVienWithEmailAndWithoutChucVuAndTrangThaiAndMaNhanVien {

    @NotNull(message = "ID khong duoc null")
    @Range(min=1,message = "ID phai lon hon hoac bang 1")
    private Long id;



    @NotBlank(message = "Ten phai tu 1 - 100 ky tu")
    @Size(min = 1, max = 100)
    private String ten;

    @NotBlank(message = "tenDem phai tu 1 - 100 ky tu")
    @Size(max = 100)
    private String tenDem;

    @NotBlank(message = "ho phai tu 1 - 50 ky tu")
    @Size(max = 50)
    private String ho;

    @NotNull(message = "Thieu ngay sinh")
    private LocalDate ngaySinh;

    @NotNull(message = "Thieu gioi tinh")
    @Range(min = 0,message = "Gioi tinh phai lon hon hoac bang 0")
    private Integer gioiTinh;



    @Size(max = 50,message = "Dan toc toi da 50 ky tu")
    private String danToc;

    @NotBlank
    @Size(max = 255, message = "Dia chi toi da 255 ky tu")
    private String diaChi;


    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 100,message = "Ghi chu toi da 100 ky tu")
    private String ghiChu;

    @Size(min=10,max=13)
    @NotBlank
    private String soDienThoai;
}
