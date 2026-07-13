package com.minimarket.controller;

import com.minimarket.controller.assembler.InventarioModelAssembler;
import com.minimarket.dto.InventarioRequestDto;
import com.minimarket.dto.InventarioResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.InventarioService;
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
@RequestMapping("/api/inventario")
@PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
@Tag(name = "Inventario", description = "Operaciones relacionadas con los movimientos de inventario")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private InventarioModelAssembler inventarioModelAssembler;

    @GetMapping
    @Operation(summary = "Listar movimientos de inventario", description = "Obtiene una lista de todos los movimientos de inventario registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimientos de inventario obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<InventarioResponseDto>> listarMovimientosDeInventario() {
        return inventarioModelAssembler.toCollectionModel(inventarioService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID", description = "Obtiene los detalles de un movimiento de inventario especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento de inventario encontrado"),
        @ApiResponse(responseCode = "404", description = "Movimiento de inventario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<InventarioResponseDto>> obtenerMovimientoPorId(@PathVariable Long id) {
        Optional<InventarioResponseDto> inventario = inventarioService.findById(id);
        return inventario.map(inventarioModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar movimiento", description = "Registra un nuevo movimiento de inventario. Se requiere un objeto InventarioRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimiento de inventario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del movimiento incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<InventarioResponseDto>> registrarMovimiento(
            @Parameter(description = "Objeto de Inventario") @Valid @RequestBody InventarioRequestDto inventario) {
        InventarioResponseDto created = inventarioService.save(inventario);
        return ResponseEntity.created(linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(created.getId())).toUri())
                .body(inventarioModelAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar movimiento", description = "Actualiza un movimiento de inventario existente utilizando su ID. Se requiere un objeto InventarioRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento de inventario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Movimiento de inventario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del movimiento incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<InventarioResponseDto>> actualizarMovimiento(
            @Parameter(description = "ID del movimiento a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto de Inventario") @Valid @RequestBody InventarioRequestDto inventario) {
        Optional<InventarioResponseDto> existente = inventarioService.findById(id);
        if (existente.isPresent()) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioModelAssembler.toModel(inventarioService.save(inventario)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar movimiento", description = "Elimina un movimiento de inventario especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento de inventario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Movimiento de inventario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Map<String, String>>> eliminarMovimiento(
            @Parameter(description = "ID del movimiento a eliminar", example = "1") @PathVariable Long id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.ok(EntityModel.of(
                    Map.of("mensaje", "Movimiento de inventario eliminado exitosamente"),
                    linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario")
            ));
        }
        return ResponseEntity.notFound().build();
    }
}
