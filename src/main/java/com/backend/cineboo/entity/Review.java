package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Entity
@Table(name = "REVIEW")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="MAREVIEW")
    private String maReview;

    @Column(name="ID_KHACHHANG")
    @NotNull
    @Range(min=1)
    private Long id_KhachHang;

    @NotNull
    @Range(min=1)
    @Column(name="ID_PHIM")
    private Long id_Phim;

    @Column(name="DANHGIA")
    @NotNull
    @Range(min=1, max=10)
    private Integer danhGia;

    @Column(name="BINHLUAN",length = 2000)
    @NotBlank
    @Size(min=1,max=2000)
    private String binhLuan;

    @Column(name="TRANGTHAIREVIEW")
    private int trangThaiReview;
}
