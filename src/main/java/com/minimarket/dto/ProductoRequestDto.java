package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear o actualizar un producto")
public class ProductoRequestDto {

    @Schema(description = "Identificador interno del producto", example = "10")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Nombre comercial del producto", example = "Arroz grado 1")
    private String nombre;

    @NotNull
    @Schema(description = "Precio unitario del producto", example = "1590.0")
    private Double precio;

    @NotNull
    @Schema(description = "Stock disponible del producto", example = "35")
    private Integer stock;

    @NotNull
    @Schema(description = "Identificador de la categoria asociada", example = "1")
    private Long categoriaId;
}