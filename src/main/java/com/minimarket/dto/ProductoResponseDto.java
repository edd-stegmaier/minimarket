package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representacion de un producto expuesto por la API")
public class ProductoResponseDto {

    @Schema(description = "Identificador del producto", example = "10")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Arroz grado 1")
    private String nombre;

    @Schema(description = "Precio unitario del producto", example = "1590.0")
    private Double precio;

    @Schema(description = "Stock disponible", example = "35")
    private Integer stock;

    @Schema(description = "Categoria asociada al producto")
    private CategoriaResponseDto categoria;
}