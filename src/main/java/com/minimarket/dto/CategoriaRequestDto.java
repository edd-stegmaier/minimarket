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
@Schema(description = "Datos requeridos para crear o actualizar una categoria")
public class CategoriaRequestDto {

    @Schema(description = "Identificador interno de la categoria", example = "1")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Nombre visible de la categoria", example = "Abarrotes")
    private String nombre;
}