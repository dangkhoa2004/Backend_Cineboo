package com.backend.cineboo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
@Getter
@Setter
public class ChiTietHoaDonListDTO {
    @NotBlank
    @Size(max = 15)
    private String maGhe;
}
