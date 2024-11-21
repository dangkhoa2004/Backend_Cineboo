package com.backend.cineboo.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSpentAmount {
    private Integer id;

    private String ten;

    private String maTaiKhoan;

    private Integer gioiTinh;

    private Integer diem;

    private BigDecimal tongSoTien;
}
