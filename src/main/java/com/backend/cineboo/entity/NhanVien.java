package com.backend.cineboo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.*;
import lombok.*;

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
    @PrimaryKeyJoinColumn(name = "ID_TAIKHOAN")
    private TaiKhoan taiKhoan;

    @Column(name = "MANHANVIEN", nullable = false, length = 15)
    private String maNhanVien;

    @Column(name = "TEN")
    private String ten;

    @Column(name = "TENDEM")
    private String tenDem;

    @Column(name = "HO")
    private String ho;

    @Column(name = "NGAYSINH")
    private LocalDate ngaySinh;

    @Column(name = "GIOITINH")
    private Integer gioiTinh;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DANTOC")
    private String danToc;

    @Column(name = "DIACHI")
    private String diaChi;

    @Column(name = "TRANGTHAINHANVIEN")
    private Integer trangThai;
}
