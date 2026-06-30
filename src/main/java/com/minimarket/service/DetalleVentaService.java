package com.minimarket.service;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DetalleVentaService {

	@Autowired
	private DetalleVentaRepository detalleVentaRepository;

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private ProductoRepository productoRepository;

	public List<DetalleVentaResponseDto> findAll() {
		return detalleVentaRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<DetalleVentaResponseDto> findById(Long id) {
		return detalleVentaRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public DetalleVentaResponseDto save(DetalleVentaRequestDto detalleVenta) {
		Long ventaId = detalleVenta.getVentaId();
		if (ventaId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El detalle debe incluir una ventaId");
		}

		Venta venta = ventaRepository.findById(Objects.requireNonNull(ventaId, "ventaId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Venta no encontrada"));
		Producto producto = productoRepository.findById(Objects.requireNonNull(detalleVenta.getProductoId(), "productoId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));

		return Mapper.toDto(Objects.requireNonNull(detalleVentaRepository.save(Objects.requireNonNull(Mapper.toEntity(detalleVenta, venta, producto)))));
	}

	public void deleteById(Long id) {
		detalleVentaRepository.deleteById(Objects.requireNonNull(id, "id"));
	}

	public List<DetalleVentaResponseDto> findByVentaId(Long ventaId) {
		return detalleVentaRepository.findByVentaId(ventaId).stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}
}