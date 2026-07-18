package com.minimarket.controller;

import com.minimarket.controller.assembler.ProductoModelAssembler;
import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.ProductoService;
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
@RequestMapping("/api/productos")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
@Tag(name = "Producto", description = "Operaciones relacionadas con los productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler productoModelAssembler;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene una lista de todos los productos registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<ProductoResponseDto>> listarProductos() {
        List<ProductoResponseDto> productos = productoService.findAll();
        return productoModelAssembler.toCollectionModel(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene los detalles de un producto especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<ProductoResponseDto>> obtenerProductoPorId(@PathVariable Long id) {
        Optional<ProductoResponseDto> producto = productoService.findById(id);
        return producto.map(productoModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema. Se requiere un objeto ProductoRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del producto incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<ProductoResponseDto>> guardarProducto(
            @Parameter(description = "Objeto de Producto") @Valid @RequestBody ProductoRequestDto producto) {
        ProductoResponseDto created = productoService.save(producto);
        EntityModel<ProductoResponseDto> model = productoModelAssembler.toModel(created);
        return ResponseEntity.created(linkTo(methodOn(ProductoController.class).obtenerProductoPorId(created.getId())).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente utilizando su ID. Se requiere un objeto ProductoRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del producto incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<ProductoResponseDto>> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto de Producto") @Valid @RequestBody ProductoRequestDto producto) {
        Optional<ProductoResponseDto> productoExistente = productoService.findById(id);
        if (productoExistente.isPresent()) {
            producto.setId(id);
            ProductoResponseDto updated = productoService.save(producto);
            return ResponseEntity.ok(productoModelAssembler.toModel(updated));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", example = "1") @PathVariable Long id) {
        if (productoService.findById(id).isPresent()) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
