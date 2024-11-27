package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@Table(name = "DANHSACHHOANVE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DanhSachHoanVe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MAHOANVE")
    private String maHoanVe;

    @OneToOne(optional = true)
    @JoinColumn(name = "ID_HOADON")
    private HoaDon hoaDon;

    @Column(name = "THOIGIANHOANVE")
    @NotNull
    private LocalDateTime thoiGianHoanVe;

    @Column(name="LYDOHOANVE", length = 50)
    @Size(max=50)
    private String lyDoHoanVe;

    @Range(min = 0, max = 255)
    @Column(name="TRANGTHAIHOANVE")
    @NotNull
    private Integer trangThaiHoanVe;
}
