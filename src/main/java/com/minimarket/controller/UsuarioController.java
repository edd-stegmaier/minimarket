package com.minimarket.controller;

import com.minimarket.dto.UsuarioRequestDto;
import com.minimarket.dto.UsuarioResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioResponseDto> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok) // Si el usuario existe, devuelve 200 OK con el usuario
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no, devuelve 404
    }

    @PostMapping
    public UsuarioResponseDto guardarUsuario(@Valid @RequestBody UsuarioRequestDto usuario) {
        return usuarioService.save(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto usuario) {
        Optional<UsuarioResponseDto> usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioService.save(usuario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<UsuarioResponseDto> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) { // Verifica si el usuario existe
            usuarioService.deleteById(id); // Elimina al usuario
            return ResponseEntity.noContent().build(); // Respuesta 204 (sin contenido)
        }
        return ResponseEntity.notFound().build(); // Respuesta 404 (no encontrado)
    }
}
