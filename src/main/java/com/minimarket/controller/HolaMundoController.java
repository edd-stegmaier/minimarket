package com.minimarket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Publico", description = "Endpoint publico de prueba")
public class HolaMundoController {

    @GetMapping("/public/hola")
    @Operation(summary = "Saludo publico", description = "Devuelve un mensaje simple de prueba para verificar el acceso publico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje devuelto exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public String holaMundo() {
        return "¡Hola Mundo!";
    }
}
