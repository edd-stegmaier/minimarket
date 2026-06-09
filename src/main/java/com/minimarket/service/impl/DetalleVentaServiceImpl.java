package com.minimarket.service.impl;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<DetalleVentaResponseDto> findAll() {
        return detalleVentaRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DetalleVentaResponseDto> findById(Long id) {
        return detalleVentaRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public DetalleVentaResponseDto save(DetalleVentaRequestDto detalleVenta) {
        Long ventaId = detalleVenta.getVentaId();
        if (ventaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El detalle debe incluir una ventaId");
        }

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venta no encontrada"));
        Producto producto = productoRepository.findById(detalleVenta.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));

        return Mapper.toDto(detalleVentaRepository.save(Mapper.toEntity(detalleVenta, venta, producto)));
    }

    @Override
    public void deleteById(Long id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public List<DetalleVentaResponseDto> findByVentaId(Long ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }
}
