package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representacion de un usuario del sistema")
public class UsuarioResponseDto {
    
    @Schema(description = "Identificador del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;

    @Schema(description = "Roles asignados al usuario")
    private Set<RolResponseDto> roles;
}
