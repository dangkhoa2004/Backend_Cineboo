package com.backend.cineboo.dto;

import com.backend.cineboo.entity.Phim;
import com.backend.cineboo.entity.PhongChieu;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
public class SuatChieuDTO {



    @NotNull
    private LocalDateTime thoiGianChieu;

    @NotNull
    private Long id_Phim;

    @NotNull
    private Long id_PhongChieu;

}
