package com.minimarket.service;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.Mapper;
import com.minimarket.dto.VentaRequestDto;
import com.minimarket.dto.VentaResponseDto;
import com.minimarket.entity.*;
import com.minimarket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VentaService {

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProductoRepository productoRepository;

	public List<VentaResponseDto> findAll() {
		return ventaRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<VentaResponseDto> findById(Long id) {
		return ventaRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public VentaResponseDto save(VentaRequestDto ventaDto) {
		Usuario usuario = usuarioRepository.findById(Objects.requireNonNull(ventaDto.getUsuarioId(), "usuarioId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

		Venta venta = Objects.requireNonNull(Mapper.toEntity(ventaDto, usuario, new ArrayList<>()));
		List<DetalleVenta> detalles = ventaDto.getDetalles().stream()
				.map(detalle -> mapDetalleVenta(detalle, venta))
				.collect(Collectors.toList());
		venta.setDetalles(detalles);

		return Mapper.toDto(Objects.requireNonNull(ventaRepository.save(Objects.requireNonNull(venta))));
	}

	public List<VentaResponseDto> findByUsuarioId(Long usuarioId) {
		return ventaRepository.findByUsuarioId(usuarioId).stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	private DetalleVenta mapDetalleVenta(DetalleVentaRequestDto detalleDto, Venta venta) {
		Producto producto = productoRepository.findById(Objects.requireNonNull(detalleDto.getProductoId(), "productoId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
		return Objects.requireNonNull(Mapper.toEntity(detalleDto, venta, producto));
	}
}