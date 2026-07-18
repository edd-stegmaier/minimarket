package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para agregar o actualizar un item del carrito")
public class CarritoRequestDto {

    @Schema(description = "Identificador interno del item del carrito", example = "3")
    private Long id;

    @NotNull
    @Schema(description = "Identificador del usuario dueno del carrito", example = "1")
    private Long usuarioId;

    @NotNull
    @Schema(description = "Identificador del producto agregado", example = "10")
    private Long productoId;

    @NotNull
    @Schema(description = "Cantidad solicitada del producto", example = "2")
    private Integer cantidad;
}