/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.entity;

import jakarta.persistence.*;
import java.math.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author 04dkh
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GheWithBookingStatus {

    private Integer id;


    private String maGhe;

 
    private BigDecimal giaTien;


    private Integer id_PhongChieu;

 
    
    private Integer trangThaiGhe;
	
	
	private Byte  trangThaiGheAndSuatChieu;
	
}
