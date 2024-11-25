package com.backend.cineboo.dto;

import jakarta.validation.Valid;
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
    private Long suatChieuId;

    @Valid
    @NotNull
    List<ChiTietHoaDonListDTO> chiTietHoaDonList;

}
