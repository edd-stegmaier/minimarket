package com.minimarket.service;

import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<CategoriaResponseDto> findAll();
    Optional<CategoriaResponseDto> findById(Long id);
    CategoriaResponseDto save(CategoriaRequestDto categoria);
    void deleteById(Long id);
}
