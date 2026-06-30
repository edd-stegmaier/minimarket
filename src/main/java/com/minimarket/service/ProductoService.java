package com.minimarket.service;

import com.minimarket.dto.Mapper;
import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.security.util.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<ProductoResponseDto> findAll() {
		return productoRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<ProductoResponseDto> findById(Long id) {
		return productoRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public ProductoResponseDto save(ProductoRequestDto producto) {
		producto.setNombre(Sanitizer.sanitizeInput(producto.getNombre()));
		Categoria categoria = categoriaRepository.findById(Objects.requireNonNull(producto.getCategoriaId(), "categoriaId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria no encontrada"));
		return Mapper.toDto(Objects.requireNonNull(productoRepository.save(Objects.requireNonNull(Mapper.toEntity(producto, categoria)))));
	}

	public void deleteById(Long id) {
		productoRepository.deleteById(Objects.requireNonNull(id, "id"));
	}

	public List<ProductoResponseDto> findByCategoriaId(Long categoriaId) {
		return productoRepository.findByCategoriaId(categoriaId).stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}
}