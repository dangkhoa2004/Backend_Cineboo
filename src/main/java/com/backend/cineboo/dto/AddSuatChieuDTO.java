package com.backend.cineboo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
public class AddSuatChieuDTO {



    @NotNull
    private LocalDateTime thoiGianChieu;

    @NotNull
    private Long id_Phim;

    @NotNull
    private Long id_PhongChieu;

}
