package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representacion de una categoria de producto")
public class CategoriaResponseDto {

    @Schema(description = "Identificador de la categoria", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoria", example = "Abarrotes")
    private String nombre;
}