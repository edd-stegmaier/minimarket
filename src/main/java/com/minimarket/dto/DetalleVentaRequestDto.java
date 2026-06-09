package com.minimarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaRequestDto {

    private Long id;
    private Long ventaId;

    @NotNull
    private Long productoId;

    @NotNull
    private Integer cantidad;

    @NotNull
    private Double precio;
}