package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para registrar una venta")
public class VentaRequestDto {

    @Schema(description = "Identificador interno de la venta", example = "8")
    private Long id;

    @NotNull
    @Schema(description = "Identificador del usuario asociado a la venta", example = "1")
    private Long usuarioId;

    @NotNull
    @Schema(description = "Fecha de la venta en formato ISO-8601", example = "2026-07-18T18:00:00Z")
    private Date fecha;

    @Valid
    @NotNull
    @Size(min = 1)
    @Schema(description = "Lineas de detalle incluidas en la venta", example = "[{\"productoId\":10,\"cantidad\":2,\"precio\":1590.0}]")
    private List<DetalleVentaRequestDto> detalles;
}