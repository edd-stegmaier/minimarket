package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaResponseDto {

    private Long id;
    private ProductoResponseDto producto;
    private Integer cantidad;
    private Double precio;
}