package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para crear o actualizar un detalle de venta")
public class DetalleVentaRequestDto {

    @Schema(description = "Identificador interno del detalle", example = "15")
    private Long id;

    @Schema(description = "Identificador de la venta asociada cuando ya existe", example = "8")
    private Long ventaId;

    @NotNull
    @Schema(description = "Identificador del producto vendido", example = "10")
    private Long productoId;

    @NotNull
    @Schema(description = "Cantidad vendida del producto", example = "2")
    private Integer cantidad;

    @NotNull
    @Schema(description = "Precio unitario aplicado al detalle", example = "1590.0")
    private Double precio;
}