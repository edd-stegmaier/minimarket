package com.minimarket.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolRequestDto {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String nombre;
}
