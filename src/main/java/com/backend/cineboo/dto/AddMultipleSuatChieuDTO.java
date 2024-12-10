package com.backend.cineboo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class AddMultipleSuatChieuDTO {



    @NotNull
    @Valid
    @Size(max = 7,min = 1)
    private List<LocalDateTime> thoiGianChieuList;

    @NotNull(message = "Thiếu id_Phim")
    private Long id_Phim;

    @NotNull(message = "Thiếu id_PhongChieu")
    private Long id_PhongChieu;

}
