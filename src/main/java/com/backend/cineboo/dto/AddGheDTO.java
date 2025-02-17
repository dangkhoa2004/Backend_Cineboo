package com.backend.cineboo.dto;

import com.backend.cineboo.entity.PhongChieu;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class AddGheDTO {
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal giaTien;

    @NotBlank
    @Size(max = 15)
    private String maGhe;

    @NotNull
    @Range(min = 0)
    private Long id_PhongChieu;

//    @NotNull
//    @Range(min = 0)
//    private Integer trangThaiGhe;
}
