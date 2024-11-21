package com.backend.cineboo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenuePerMovie {

    private String tenPhim;

    private Long luotMua;

    private BigDecimal doanhThu;

    private List<String> theLoai;

    private Integer nam;
}
