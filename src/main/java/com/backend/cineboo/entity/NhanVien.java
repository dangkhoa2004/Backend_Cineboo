package com.backend.cineboo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "NHANVIEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CHUCVU")
    private PhanLoaiChucVu chucVu;

    @JsonIgnoreProperties("matKhau")
    @OneToOne
    @JoinColumn(name = "ID_TAIKHOAN")
    private TaiKhoan taiKhoan;

    @Column(name = "MANHANVIEN", nullable = false, length = 15)
    private String maNhanVien;

    @Column(name = "TEN",nullable = false)
    @NotBlank
    @Size(max = 100)
    private String ten;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TENDEM")
    private String tenDem;

    @NotBlank
    @Size(max = 50)
    @Column(name = "HO")
    private String ho;

    @NotNull
    @Column(name = "NGAYSINH",nullable = false)
    private LocalDate ngaySinh;

    @NotNull
    @Column(name = "GIOITINH",nullable = false)
    private Integer gioiTinh;



    @Size(max = 50)
    @Column(name = "DANTOC")
    private String danToc;

    @NotBlank
    @Size(max = 255)
    @Column(name = "DIACHI",nullable = false)
    private String diaChi;

    @Column(name = "TRANGTHAINHANVIEN")
    private Integer trangThai;


    @Column(name = "SODIENTHOAI")
    private String soDienThoai;
}
