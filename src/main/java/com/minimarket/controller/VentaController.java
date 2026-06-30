package com.minimarket.controller;

import com.minimarket.dto.VentaRequestDto;
import com.minimarket.dto.VentaResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
@PreAuthorize("hasAnyAuthority('CLIENTE', 'EMPLEADO', 'ADMINISTRADOR')")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<VentaResponseDto> listarVentas() {
        return ventaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDto> obtenerVentaPorId(@PathVariable Long id) {
        Optional<VentaResponseDto> venta = ventaService.findById(id);
        return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public VentaResponseDto guardarVenta(@Valid @RequestBody VentaRequestDto venta) {
        return ventaService.save(venta);
    }
}
