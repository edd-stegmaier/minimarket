package com.minimarket.controller;

import com.minimarket.dto.InventarioRequestDto;
import com.minimarket.dto.InventarioResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario")
@PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<InventarioResponseDto> listarMovimientosDeInventario() {
        return inventarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDto> obtenerMovimientoPorId(@PathVariable Long id) {
        Optional<InventarioResponseDto> inventario = inventarioService.findById(id);
        return inventario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public InventarioResponseDto registrarMovimiento(@Valid @RequestBody InventarioRequestDto inventario) {
        return inventarioService.save(inventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDto> actualizarMovimiento(@PathVariable Long id, @Valid @RequestBody InventarioRequestDto inventario) {
        Optional<InventarioResponseDto> existente = inventarioService.findById(id);
        if (existente.isPresent()) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioService.save(inventario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
