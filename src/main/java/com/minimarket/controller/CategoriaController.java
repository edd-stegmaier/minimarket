package com.minimarket.controller;

import com.minimarket.controller.assembler.CategoriaModelAssembler;
import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
@Tag(name = "Categoria", description = "Operaciones relacionadas con las categorias de productos")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler categoriaModelAssembler;

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Obtiene una lista de todas las categorias registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorias obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<CategoriaResponseDto>> listarCategorias() {
        return categoriaModelAssembler.toCollectionModel(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoria por ID", description = "Obtiene los detalles de una categoria especifica utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CategoriaResponseDto>> obtenerCategoriaPorId(@PathVariable Long id) {
        Optional<CategoriaResponseDto> categoria = categoriaService.findById(id);
        return categoria.map(categoriaModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
    @Operation(summary = "Crear categoria", description = "Registra una nueva categoria en el sistema. Se requiere un objeto CategoriaRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos de la categoria incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CategoriaResponseDto>> guardarCategoria(
            @Parameter(description = "Objeto de Categoria") @Valid @RequestBody CategoriaRequestDto categoria) {
        CategoriaResponseDto created = categoriaService.save(categoria);
        return ResponseEntity.created(linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(created.getId())).toUri())
                .body(categoriaModelAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
    @Operation(summary = "Actualizar categoria", description = "Actualiza una categoria existente utilizando su ID. Se requiere un objeto CategoriaRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos de la categoria incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CategoriaResponseDto>> actualizarCategoria(
            @Parameter(description = "ID de la categoria a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto de Categoria") @Valid @RequestBody CategoriaRequestDto categoria) {
        Optional<CategoriaResponseDto> categoriaExistente = categoriaService.findById(id);
        if (categoriaExistente.isPresent()) {
            categoria.setId(id);
            return ResponseEntity.ok(categoriaModelAssembler.toModel(categoriaService.save(categoria)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria especifica utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoria a eliminar", example = "1") @PathVariable Long id) {
        if (categoriaService.findById(id).isPresent()) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
