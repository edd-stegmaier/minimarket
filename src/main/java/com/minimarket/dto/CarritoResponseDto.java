package com.minimarket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representacion de un item del carrito de compras")
public class CarritoResponseDto {

    @Schema(description = "Identificador del item del carrito", example = "3")
    private Long id;

    @Schema(description = "Usuario propietario del carrito")
    private UsuarioResponseDto usuario;

    @Schema(description = "Producto agregado al carrito")
    private ProductoResponseDto producto;

    @Schema(description = "Cantidad del producto agregado", example = "2")
    private Integer cantidad;
}