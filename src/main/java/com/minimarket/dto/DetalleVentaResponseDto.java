package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representacion de una linea de detalle de venta")
public class DetalleVentaResponseDto {

    @Schema(description = "Identificador del detalle de venta", example = "15")
    private Long id;

    @Schema(description = "Producto vendido en el detalle")
    private ProductoResponseDto producto;

    @Schema(description = "Cantidad registrada en la linea", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario registrado en la linea", example = "1590.0")
    private Double precio;
}