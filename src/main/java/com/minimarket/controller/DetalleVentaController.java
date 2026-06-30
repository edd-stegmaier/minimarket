package com.minimarket.controller;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-ventas")
@PreAuthorize("hasAnyAuthority('EMPLEADO', 'ADMINISTRADOR')")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    public List<DetalleVentaResponseDto> listarDetalleVentas() {
        return detalleVentaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDto> obtenerDetalleVentaPorId(@PathVariable Long id) {
        Optional<DetalleVentaResponseDto> detalleVenta = detalleVentaService.findById(id);
        return detalleVenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public DetalleVentaResponseDto guardarDetalleVenta(@Valid @RequestBody DetalleVentaRequestDto detalleVenta) {
        return detalleVentaService.save(detalleVenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDto> actualizarDetalleVenta(@PathVariable Long id, @Valid @RequestBody DetalleVentaRequestDto detalleVenta) {
        Optional<DetalleVentaResponseDto> existente = detalleVentaService.findById(id);
        if (existente.isPresent()) {
            detalleVenta.setId(id);
            return ResponseEntity.ok(detalleVentaService.save(detalleVenta));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable Long id) {
        if (detalleVentaService.findById(id).isPresent()) {
            detalleVentaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
