package com.minimarket.service;

import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;

import java.util.List;
import java.util.Optional;

public interface CarritoService {
    List<CarritoResponseDto> findAll();
    Optional<CarritoResponseDto> findById(Long id);
    CarritoResponseDto save(CarritoRequestDto carrito);
    void deleteById(Long id);
    List<CarritoResponseDto> findByUsuarioId(Long usuarioId);
}
