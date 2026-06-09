package com.minimarket.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {
    
    private Long id;
    private String username;
    private Set<RolResponseDto> roles;
}
