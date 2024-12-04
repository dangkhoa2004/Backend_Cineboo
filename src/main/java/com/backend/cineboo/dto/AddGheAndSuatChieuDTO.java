package com.backend.cineboo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@Getter
@Setter
public class AddGheAndSuatChieuDTO {
    @NotNull
    private Long id_PhongChieu;

    @NotNull
    private Long id_SuatChieu;
    
}
