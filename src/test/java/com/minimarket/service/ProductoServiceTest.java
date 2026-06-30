package com.minimarket.service;

import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

	@Mock
	private ProductoRepository productoRepository;

	@Mock
	private CategoriaRepository categoriaRepository;

	@InjectMocks
	private ProductoService productoService;

	private Categoria categoria;
	private Producto producto;
	private ProductoRequestDto productoRequestDto;

	@BeforeEach
	void setUp() {
		categoria = new Categoria();
		categoria.setId(1L);
		categoria.setNombre("Abarrotes");

		producto = new Producto();
		producto.setId(2L);
		producto.setNombre("Arroz");
		producto.setPrecio(1500.0);
		producto.setStock(20);
		producto.setCategoria(categoria);

		productoRequestDto = new ProductoRequestDto();
		productoRequestDto.setId(2L);
		productoRequestDto.setNombre("Arroz");
		productoRequestDto.setPrecio(1500.0);
		productoRequestDto.setStock(20);
		productoRequestDto.setCategoriaId(1L);
	}

	@Test
	void testFindAll() {
		when(productoRepository.findAll()).thenReturn(List.of(producto));

		List<ProductoResponseDto> resultado = productoService.findAll();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals(2L, resultado.get(0).getId());
		assertEquals("Arroz", resultado.get(0).getNombre());
		assertEquals(1500.0, resultado.get(0).getPrecio());
		assertEquals(20, resultado.get(0).getStock());
		assertEquals(1L, resultado.get(0).getCategoria().getId());
		verify(productoRepository).findAll();
	}

	@Test
	void testFindById() {
		when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));

		Optional<ProductoResponseDto> resultado = productoService.findById(2L);

		assertTrue(resultado.isPresent());
		assertEquals(2L, resultado.get().getId());
		assertEquals("Arroz", resultado.get().getNombre());
		verify(productoRepository).findById(2L);
	}

	@Test
	void testSave() {
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
		when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ProductoResponseDto resultado = productoService.save(productoRequestDto);

		assertNotNull(resultado);
		assertEquals(2L, resultado.getId());
		assertEquals("Arroz", resultado.getNombre());
		assertEquals(1500.0, resultado.getPrecio());
		assertEquals(20, resultado.getStock());
		assertEquals(1L, resultado.getCategoria().getId());
		verify(categoriaRepository).findById(1L);
		verify(productoRepository).save(any(Producto.class));
	}

	@Test
	void testDeleteById() {
		doNothing().when(productoRepository).deleteById(2L);

		productoService.deleteById(2L);

		verify(productoRepository).deleteById(2L);
	}

	@Test
	void testFindByCategoriaId() {
		when(productoRepository.findByCategoriaId(1L)).thenReturn(List.of(producto));

		List<ProductoResponseDto> resultado = productoService.findByCategoriaId(1L);

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals(2L, resultado.get(0).getId());
		assertEquals("Arroz", resultado.get(0).getNombre());
		assertEquals(1L, resultado.get(0).getCategoria().getId());
		verify(productoRepository).findByCategoriaId(1L);
	}
}
