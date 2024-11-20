package com.backend.cineboo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AddTheLoaiPhimDTO {
    @NotBlank
    @Size(max=100)
    private String tenTheLoai;
}
