package com.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioResponseDto {

    private Long id;
    private ProductoResponseDto producto;
    private Integer cantidad;
    private String tipoMovimiento;
    private Date fechaMovimiento;
}