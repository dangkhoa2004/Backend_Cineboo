/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author 04dkh
 */
@Entity
@Table(name = "PTTT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PTTT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MAPTTT", length = 15)
    @NotBlank
    @Size(max = 15)
    private String maPTTT;


    @Column(name = "TENPTTT", length = 200)
    @NotBlank
    @Size(max = 200)
    private String tenPTTT;

    @Column(name = "TRANGTHAIPTTT")
    @Range(min = 0)
    @NotNull
    private Integer trangThaiPTTT;
}
