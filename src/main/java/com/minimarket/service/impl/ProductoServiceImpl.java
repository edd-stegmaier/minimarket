package com.minimarket.service.impl;

import com.minimarket.dto.Mapper;
import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.entity.Categoria;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.security.util.Sanitizer;
import com.minimarket.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<ProductoResponseDto> findAll() {
        return productoRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductoResponseDto> findById(Long id) {
        return productoRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public ProductoResponseDto save(ProductoRequestDto producto) {
        producto.setNombre(Sanitizer.sanitizeInput(producto.getNombre()));
        Categoria categoria = categoriaRepository.findById(producto.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria no encontrada"));
        return Mapper.toDto(productoRepository.save(Mapper.toEntity(producto, categoria)));
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoResponseDto> findByCategoriaId(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }
}
