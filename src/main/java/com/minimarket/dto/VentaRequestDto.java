package com.minimarket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaRequestDto {

    private Long id;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Date fecha;

    @Valid
    @NotNull
    @Size(min = 1)
    private List<DetalleVentaRequestDto> detalles;
}