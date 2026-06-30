package com.minimarket.controller;

import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoResponseDto> listarProductos() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerProductoPorId(@PathVariable Long id) {
        Optional<ProductoResponseDto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ProductoResponseDto guardarProducto(@Valid @RequestBody ProductoRequestDto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<ProductoResponseDto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequestDto producto) {
        Optional<ProductoResponseDto> productoExistente = productoService.findById(id);
        if (productoExistente.isPresent()) {
            producto.setId(id);
            return ResponseEntity.ok(productoService.save(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.findById(id).isPresent()) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
