package com.minimarket.controller;

import com.minimarket.controller.assembler.DetalleVentaModelAssembler;
import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.DetalleVentaService;
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
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/detalle-ventas")
@PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
@Tag(name = "Detalle de Venta", description = "Operaciones relacionadas con los detalles de ventas")
@SecurityRequirement(name = "bearerAuth")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private DetalleVentaModelAssembler detalleVentaModelAssembler;

    @GetMapping
    @Operation(summary = "Listar detalles de venta", description = "Obtiene una lista de todos los detalles de venta registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles de venta obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<DetalleVentaResponseDto>> listarDetalleVentas() {
        return detalleVentaModelAssembler.toCollectionModel(detalleVentaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de venta por ID", description = "Obtiene los detalles de una linea de venta especifica utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de venta encontrado"),
        @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<DetalleVentaResponseDto>> obtenerDetalleVentaPorId(@PathVariable Long id) {
        Optional<DetalleVentaResponseDto> detalleVenta = detalleVentaService.findById(id);
        return detalleVenta.map(detalleVentaModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear detalle de venta", description = "Registra un nuevo detalle de venta. Se requiere un objeto DetalleVentaRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle de venta creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del detalle incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<DetalleVentaResponseDto>> guardarDetalleVenta(
            @Parameter(description = "Objeto de Detalle de Venta") @Valid @RequestBody DetalleVentaRequestDto detalleVenta) {
        DetalleVentaResponseDto created = detalleVentaService.save(detalleVenta);
        return ResponseEntity.created(linkTo(methodOn(DetalleVentaController.class).obtenerDetalleVentaPorId(created.getId())).toUri())
                .body(detalleVentaModelAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle de venta", description = "Actualiza un detalle de venta existente utilizando su ID. Se requiere un objeto DetalleVentaRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de venta actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del detalle incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<DetalleVentaResponseDto>> actualizarDetalleVenta(
            @Parameter(description = "ID del detalle de venta a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto de Detalle de Venta") @Valid @RequestBody DetalleVentaRequestDto detalleVenta) {
        Optional<DetalleVentaResponseDto> existente = detalleVentaService.findById(id);
        if (existente.isPresent()) {
            detalleVenta.setId(id);
            return ResponseEntity.ok(detalleVentaModelAssembler.toModel(detalleVentaService.save(detalleVenta)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle de venta", description = "Elimina un detalle de venta especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle de venta eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Map<String, String>>> eliminarDetalleVenta(
            @Parameter(description = "ID del detalle de venta a eliminar", example = "1") @PathVariable Long id) {
        if (detalleVentaService.findById(id).isPresent()) {
            detalleVentaService.deleteById(id);
            return ResponseEntity.ok(EntityModel.of(
                    Map.of("mensaje", "Detalle de venta eliminado exitosamente"),
                    linkTo(methodOn(DetalleVentaController.class).listarDetalleVentas()).withRel("detalleVentas")
            ));
        }
        return ResponseEntity.notFound().build();
    }
}
