package com.minimarket.controller;

import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;
import jakarta.validation.Valid;
import com.minimarket.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<CarritoResponseDto> listarCarrito() {
        return carritoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> obtenerCarritoPorId(@PathVariable Long id) {
        Optional<CarritoResponseDto> carrito = carritoService.findById(id);
        return carrito.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CarritoResponseDto agregarProductoAlCarrito(@Valid @RequestBody CarritoRequestDto carrito) {
        return carritoService.save(carrito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDto> actualizarCarrito(@PathVariable Long id, @Valid @RequestBody CarritoRequestDto carrito) {
        Optional<CarritoResponseDto> existente = carritoService.findById(id);
        if (existente.isPresent()) {
            carrito.setId(id);
            return ResponseEntity.ok(carritoService.save(carrito));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        if (carritoService.findById(id).isPresent()) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
