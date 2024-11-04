/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "MAPTTT")
    private String maPTTT;

    @Column(name = "TENPTTT")
    private String tenPTTT;

    @Column(name = "TRANGTHAIPTTT")
    private Integer trangThaiPTTT;
}
