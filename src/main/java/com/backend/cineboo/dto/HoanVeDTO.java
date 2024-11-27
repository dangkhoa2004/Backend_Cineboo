package com.backend.cineboo.dto;

import com.backend.cineboo.entity.HoaDon;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class HoanVeDTO {

    @NotNull
    @Range(min = 1)
    private Long hoaDonId;


    @Size(max=50)
    private String lyDoHoanVe;
}
