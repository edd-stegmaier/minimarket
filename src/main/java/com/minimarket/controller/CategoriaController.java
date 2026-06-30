package com.minimarket.controller;

import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponseDto> listarCategorias() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> obtenerCategoriaPorId(@PathVariable Long id) {
        Optional<CategoriaResponseDto> categoria = categoriaService.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
    public CategoriaResponseDto guardarCategoria(@Valid @RequestBody CategoriaRequestDto categoria) {
        return categoriaService.save(categoria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<CategoriaResponseDto> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDto categoria) {
        Optional<CategoriaResponseDto> categoriaExistente = categoriaService.findById(id);
        if (categoriaExistente.isPresent()) {
            categoria.setId(id);
            return ResponseEntity.ok(categoriaService.save(categoria));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        if (categoriaService.findById(id).isPresent()) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
