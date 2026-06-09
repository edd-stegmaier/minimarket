package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponseDto {

    private Long id;
    private UsuarioResponseDto usuario;
    private Date fecha;
    private List<DetalleVentaResponseDto> detalles;
}