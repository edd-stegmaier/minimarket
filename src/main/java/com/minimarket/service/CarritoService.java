package com.minimarket.service;

import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;
import com.minimarket.dto.Mapper;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarritoService {

	@Autowired
	private CarritoRepository carritoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProductoRepository productoRepository;

	public List<CarritoResponseDto> findAll() {
		return carritoRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<CarritoResponseDto> findById(Long id) {
		return carritoRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public CarritoResponseDto save(CarritoRequestDto carrito) {
		Usuario usuario = usuarioRepository.findById(Objects.requireNonNull(carrito.getUsuarioId(), "usuarioId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));
		Producto producto = productoRepository.findById(Objects.requireNonNull(carrito.getProductoId(), "productoId"))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no encontrado"));
		return Mapper.toDto(Objects.requireNonNull(carritoRepository.save(Objects.requireNonNull(Mapper.toEntity(carrito, usuario, producto)))));
	}

	public void deleteById(Long id) {
		carritoRepository.deleteById(Objects.requireNonNull(id, "id"));
	}

	public List<CarritoResponseDto> findByUsuarioId(Long usuarioId) {
		return carritoRepository.findByUsuarioId(usuarioId).stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}
}