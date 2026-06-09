package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResponseDto {

    private Long id;
    private UsuarioResponseDto usuario;
    private ProductoResponseDto producto;
    private Integer cantidad;
}