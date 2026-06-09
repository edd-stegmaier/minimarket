package com.minimarket.service;

import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<ProductoResponseDto> findAll();
    Optional<ProductoResponseDto> findById(Long id);
    ProductoResponseDto save(ProductoRequestDto producto);
    void deleteById(Long id);
    List<ProductoResponseDto> findByCategoriaId(Long categoriaId);
}
