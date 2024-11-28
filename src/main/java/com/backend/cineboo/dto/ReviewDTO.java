package com.backend.cineboo.dto;

import jakarta.persistence.Column;
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
public class ReviewDTO {
    @NotNull
    @Range(min=1)
    private Long id_KhachHang;

    @NotNull
    @Range(min=1)
    private Long id_Phim;

    @NotNull
    @Range(min=1, max=10)
    private Integer danhGia;

    @NotBlank
    @Size(min=1,max=2000)
    private String binhLuan;
}
