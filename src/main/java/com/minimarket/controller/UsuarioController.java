package com.minimarket.controller;

import com.minimarket.controller.assembler.UsuarioModelAssembler;
import com.minimarket.dto.UsuarioRequestDto;
import com.minimarket.dto.UsuarioResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@PreAuthorize("hasAuthority('ADMINISTRADOR')")
@Tag(name = "Usuario", description = "Operaciones relacionadas con los usuarios del sistema")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CollectionModel<EntityModel<UsuarioResponseDto>> listarUsuarios() {
        return usuarioModelAssembler.toCollectionModel(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene los detalles de un usuario especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<UsuarioResponseDto>> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioResponseDto> usuario = usuarioService.findById(id);
        return usuario.map(usuarioModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema. Se requiere un objeto UsuarioRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del usuario incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<UsuarioResponseDto>> guardarUsuario(
            @Parameter(description = "Objeto de Usuario") @Valid @RequestBody UsuarioRequestDto usuario) {
        UsuarioResponseDto created = usuarioService.save(usuario);
        return ResponseEntity.created(linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(created.getId())).toUri())
                .body(usuarioModelAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente utilizando su ID. Se requiere un objeto UsuarioRequestDto valido en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida, datos del usuario incorrectos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<UsuarioResponseDto>> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto de Usuario") @Valid @RequestBody UsuarioRequestDto usuario) {
        Optional<UsuarioResponseDto> usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioModelAssembler.toModel(usuarioService.save(usuario)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1") @PathVariable Long id) {
        Optional<UsuarioResponseDto> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
