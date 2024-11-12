package com.backend.cineboo.dto;

import com.backend.cineboo.entity.ChiTietHoaDon;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class AddHoaDonDTO {

    @NotNull
    private Long khachHangId;

    @NotNull
    private Long phimId;

    @NotNull
    List<ChiTietHoaDon> chiTietHoaDonList;

}
