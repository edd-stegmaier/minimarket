package com.minimarket.service.impl;

import com.minimarket.dto.InventarioRequestDto;
import com.minimarket.dto.InventarioResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<InventarioResponseDto> findAll() {
        return inventarioRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventarioResponseDto> findById(Long id) {
        return inventarioRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public InventarioResponseDto save(InventarioRequestDto inventario) {
        Producto producto = productoRepository.findById(inventario.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
        return Mapper.toDto(inventarioRepository.save(Mapper.toEntity(inventario, producto)));
    }

    @Override
    public void deleteById(Long id) {
        inventarioRepository.deleteById(id);
    }

    @Override
    public List<InventarioResponseDto> findByProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }
}
