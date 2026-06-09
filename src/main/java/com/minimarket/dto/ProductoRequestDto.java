package com.minimarket.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequestDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String nombre;

    @NotNull
    private Double precio;

    @NotNull
    private Integer stock;

    @NotNull
    private Long categoriaId;
}