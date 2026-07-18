package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para registrar o actualizar un usuario")
public class UsuarioRequestDto {
    
    @Schema(description = "Identificador interno del usuario", example = "1")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Schema(description = "Nombre de usuario unico", example = "admin")
    private String username;

    @NotNull
    @Size(min = 8, max = 100)
    @Schema(description = "Contrasena en texto plano antes de persistirse cifrada", example = "Segura123!")
    private String password;

    @NotNull
    @Size(min = 1)
    @Schema(description = "Identificadores de roles asignados al usuario", example = "[1, 2]")
    private Set<Long> roleIds;

}
