package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rol asignado a un usuario")
public class RolResponseDto {
    
    @Schema(description = "Identificador del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "ADMINISTRADOR")
    private String nombre;
}
