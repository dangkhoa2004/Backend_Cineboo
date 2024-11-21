package com.backend.cineboo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenuePerMonth {

    String month;

    Long totalInvoices;

    BigDecimal totalAmount;
}
