package com.minimarket.controller;

import com.minimarket.controller.assembler.VentaModelAssembler;
import com.minimarket.dto.VentaRequestDto;
import com.minimarket.dto.VentaResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ventas")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
@Tag(name = "Venta", description = "Operaciones relacionadas con las ventas")
@SecurityRequirement(name = "bearerAuth")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaModelAssembler ventaModelAssembler;

    @GetMapping
    @Operation(summary = "Listar ventas", description = "Obtiene una lista de todas las ventas registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<VentaResponseDto>> listarVentas() {
        return ventaModelAssembler.toCollectionModel(ventaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID", description = "Obtiene los detalles de una venta especifica utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<VentaResponseDto>> obtenerVentaPorId(@PathVariable Long id) {
        Optional<VentaResponseDto> venta = ventaService.findById(id);
        return venta.map(ventaModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLEADO')")
    @Operation(summary = "Crear venta", description = "Registra una nueva venta en el sistema. Se requiere un objeto VentaRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos de la venta incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<VentaResponseDto>> guardarVenta(
            @Parameter(description = "Objeto de Venta") @Valid @RequestBody VentaRequestDto venta) {
        VentaResponseDto created = ventaService.save(venta);
        return ResponseEntity.created(linkTo(methodOn(VentaController.class).obtenerVentaPorId(created.getId())).toUri())
                .body(ventaModelAssembler.toModel(created));
    }
}
