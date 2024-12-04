package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Table(name = "GHEANDSUATCHIEU")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GheAndSuatChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="ID_GHE")
    private Ghe id_Ghe;

    @NotNull
    @ManyToOne
    @JoinColumn(name="ID_SUATCHIEU")
    private SuatChieu id_SuatChieu;


    @NotNull
    @Column(name="TRANGTHAIGHEANDSUATCHIEU")
    @Range(min =0, max =255)
    private Integer trangThaiGheAndSuatChieu;

}
