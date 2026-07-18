package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representacion de una venta registrada")
public class VentaResponseDto {

    @Schema(description = "Identificador de la venta", example = "8")
    private Long id;

    @Schema(description = "Usuario asociado a la venta")
    private UsuarioResponseDto usuario;

    @Schema(description = "Fecha efectiva de la venta", example = "2026-07-18T18:00:00Z")
    private Date fecha;

    @Schema(description = "Detalle de productos incluidos en la venta")
    private List<DetalleVentaResponseDto> detalles;
}