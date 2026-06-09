package com.minimarket.service.impl;

import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<CarritoResponseDto> findAll() {
        return carritoRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CarritoResponseDto> findById(Long id) {
        return carritoRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public CarritoResponseDto save(CarritoRequestDto carrito) {
        Usuario usuario = usuarioRepository.findById(carrito.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));
        Producto producto = productoRepository.findById(carrito.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
        return Mapper.toDto(carritoRepository.save(Mapper.toEntity(carrito, usuario, producto)));
    }

    @Override
    public void deleteById(Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public List<CarritoResponseDto> findByUsuarioId(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }
}
