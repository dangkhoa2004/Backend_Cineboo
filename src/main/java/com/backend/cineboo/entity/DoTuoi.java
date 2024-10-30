package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DOTUOI")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoTuoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="MADOTUOI")
    private String maDoTuoi;

    @Column(name="TENDOTUOI")
    private String TenDoTuoi;
}
