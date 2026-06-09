package com.minimarket.service.impl;

import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.security.util.Sanitizer;
import com.minimarket.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaResponseDto> findAll() {
        return categoriaRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoriaResponseDto> findById(Long id) {
        return categoriaRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public CategoriaResponseDto save(CategoriaRequestDto categoria) {
        categoria.setNombre(Sanitizer.sanitizeInput(categoria.getNombre()));
        return Mapper.toDto(categoriaRepository.save(Mapper.toEntity(categoria)));
    }

    @Override
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }
}
