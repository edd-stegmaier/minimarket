package com.minimarket.controller;

import com.minimarket.controller.assembler.CarritoModelAssembler;
import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carrito")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
@Tag(name = "Carrito", description = "Operaciones relacionadas con el carrito de compras")
@SecurityRequirement(name = "bearerAuth")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoModelAssembler carritoModelAssembler;

    @GetMapping
    @Operation(summary = "Listar productos en el carrito", description = "Obtiene una lista de todos los productos actualmente en el carrito de compras del usuario autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos en el carrito obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<CarritoResponseDto>> listarCarrito() {
        return carritoModelAssembler.toCollectionModel(carritoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto del carrito por ID", description = "Obtiene los detalles de un producto específico en el carrito de compras utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado en el carrito"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CarritoResponseDto>> obtenerCarritoPorId(
            @Parameter(description = "ID del Carrito a Obtener", example = "1") @PathVariable Long id) {
        Optional<CarritoResponseDto> carrito = carritoService.findById(id);
        return carrito.map(carritoModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Agregar producto al carrito", description = "Agrega un nuevo producto al carrito de compras del usuario autenticado. Se requiere un objeto CarritoRequestDto válido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto agregado al carrito exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del producto incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CarritoResponseDto>> agregarProductoAlCarrito(
            @Parameter(description = "Objeto de Carrito de Compras") @Valid @RequestBody CarritoRequestDto carrito) {
        CarritoResponseDto created = carritoService.save(carrito);
        return ResponseEntity.created(linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(created.getId())).toUri())
                .body(carritoModelAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto en el carrito", description = "Actualiza los detalles de un producto existente en el carrito de compras utilizando su ID. Se requiere un objeto CarritoRequestDto válido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del producto incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CarritoResponseDto>> actualizarCarrito(
            @Parameter(description = "ID del Carrito a Actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto de Carrito de Compras") @Valid @RequestBody CarritoRequestDto carrito) {

        Optional<CarritoResponseDto> existente = carritoService.findById(id);
        if (existente.isPresent()) {
            carrito.setId(id);
            return ResponseEntity.ok(carritoModelAssembler.toModel(carritoService.save(carrito)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto del carrito", description = "Elimina un producto específico del carrito de compras utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado del carrito exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Map<String, String>>> eliminarProductoDelCarrito(
            @Parameter(description = "ID del Carrito a Eliminar", example = "1") @PathVariable Long id) {

        if (carritoService.findById(id).isPresent()) {
            carritoService.deleteById(id);
            return ResponseEntity.ok(EntityModel.of(
                    Map.of("mensaje", "Producto eliminado del carrito exitosamente"),
                    linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito")
            ));
        }
        return ResponseEntity.notFound().build();
    }
}
