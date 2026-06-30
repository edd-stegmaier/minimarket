package com.minimarket.service;

import com.minimarket.dto.InventarioRequestDto;
import com.minimarket.dto.InventarioResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InventarioService {

	@Autowired
	private InventarioRepository inventarioRepository;

	@Autowired
	private ProductoRepository productoRepository;

	public List<InventarioResponseDto> findAll() {
		return inventarioRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<InventarioResponseDto> findById(Long id) {
		return inventarioRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public InventarioResponseDto save(InventarioRequestDto inventario) {
		Producto producto = productoRepository.findById(Objects.requireNonNull(inventario.getProductoId(), "productoId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
		return Mapper.toDto(Objects.requireNonNull(inventarioRepository.save(Objects.requireNonNull(Mapper.toEntity(inventario, producto)))));
	}

	public void deleteById(Long id) {
		inventarioRepository.deleteById(Objects.requireNonNull(id, "id"));
	}

	public List<InventarioResponseDto> findByProductoId(Long productoId) {
		return inventarioRepository.findByProductoId(productoId).stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}
}