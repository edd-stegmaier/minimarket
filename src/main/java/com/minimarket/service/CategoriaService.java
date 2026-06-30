package com.minimarket.service;

import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.security.util.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<CategoriaResponseDto> findAll() {
		return categoriaRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<CategoriaResponseDto> findById(Long id) {
		return categoriaRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public CategoriaResponseDto save(CategoriaRequestDto categoria) {
		categoria.setNombre(Sanitizer.sanitizeInput(categoria.getNombre()));
		return Mapper.toDto(Objects.requireNonNull(categoriaRepository.save(Objects.requireNonNull(Mapper.toEntity(categoria)))));
	}

	public void deleteById(Long id) {
		categoriaRepository.deleteById(Objects.requireNonNull(id, "id"));
	}
}