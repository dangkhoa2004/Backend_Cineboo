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
public class AddPTTTDTO {
    @NotBlank
    @Size(max = 200)
    private String tenPTTT;

    @Range(min = 0)
    @NotNull
    private Integer trangThaiPTTT;
}
