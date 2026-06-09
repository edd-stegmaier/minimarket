package com.minimarket.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioRequestDto {

    private Long id;

    @NotNull
    private Long productoId;

    @NotNull
    private Integer cantidad;

    @NotNull
    @Size(min = 3, max = 20)
    private String tipoMovimiento;

    @NotNull
    private Date fechaMovimiento;
}