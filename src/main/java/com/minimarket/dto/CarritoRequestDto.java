package com.minimarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoRequestDto {

    private Long id;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long productoId;

    @NotNull
    private Integer cantidad;
}