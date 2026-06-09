package com.minimarket.service;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;

import java.util.List;
import java.util.Optional;

public interface DetalleVentaService {
    List<DetalleVentaResponseDto> findAll();
    Optional<DetalleVentaResponseDto> findById(Long id);
    DetalleVentaResponseDto save(DetalleVentaRequestDto detalleVenta);
    void deleteById(Long id);
    List<DetalleVentaResponseDto> findByVentaId(Long ventaId);
}
