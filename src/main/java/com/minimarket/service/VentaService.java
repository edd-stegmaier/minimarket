package com.minimarket.service;

import com.minimarket.dto.VentaRequestDto;
import com.minimarket.dto.VentaResponseDto;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    List<VentaResponseDto> findAll();
    Optional<VentaResponseDto> findById(Long id);
    VentaResponseDto save(VentaRequestDto venta);
    List<VentaResponseDto> findByUsuarioId(Long usuarioId);
}
