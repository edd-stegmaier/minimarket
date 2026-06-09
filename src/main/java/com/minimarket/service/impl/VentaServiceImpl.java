package com.minimarket.service.impl;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.Mapper;
import com.minimarket.dto.VentaRequestDto;
import com.minimarket.dto.VentaResponseDto;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<VentaResponseDto> findAll() {
        return ventaRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VentaResponseDto> findById(Long id) {
        return ventaRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public VentaResponseDto save(VentaRequestDto ventaDto) {
        Usuario usuario = usuarioRepository.findById(ventaDto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        Venta venta = Mapper.toEntity(ventaDto, usuario, new ArrayList<>());
        List<DetalleVenta> detalles = ventaDto.getDetalles().stream()
                .map(detalle -> mapDetalleVenta(detalle, venta))
                .collect(Collectors.toList());
        venta.setDetalles(detalles);

        return Mapper.toDto(ventaRepository.save(venta));
    }

    @Override
    public List<VentaResponseDto> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    private DetalleVenta mapDetalleVenta(DetalleVentaRequestDto detalleDto, Venta venta) {
        Producto producto = productoRepository.findById(detalleDto.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
        return Mapper.toEntity(detalleDto, venta, producto);
    }
}
