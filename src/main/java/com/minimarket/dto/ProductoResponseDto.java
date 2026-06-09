package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDto {

    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private CategoriaResponseDto categoria;
}