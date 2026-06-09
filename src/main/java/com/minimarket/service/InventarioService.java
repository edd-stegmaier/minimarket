package com.minimarket.service;

import com.minimarket.dto.InventarioRequestDto;
import com.minimarket.dto.InventarioResponseDto;

import java.util.List;
import java.util.Optional;

public interface InventarioService {
    List<InventarioResponseDto> findAll();
    Optional<InventarioResponseDto> findById(Long id);
    InventarioResponseDto save(InventarioRequestDto inventario);
    void deleteById(Long id);
    List<InventarioResponseDto> findByProductoId(Long productoId);
}
