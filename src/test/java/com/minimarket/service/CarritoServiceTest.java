package com.minimarket.service;

import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;
import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.UsuarioRepository;

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
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Usuario usuario;
    private Producto producto;
    private Carrito carrito;
    private CarritoRequestDto carritoRequestDto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("Juan");

        producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Arroz");

        carrito = new Carrito();
        carrito.setId(3L);
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(2);

        carritoRequestDto = new CarritoRequestDto();
        carritoRequestDto.setId(3L);
        carritoRequestDto.setUsuarioId(1L);
        carritoRequestDto.setProductoId(2L);
        carritoRequestDto.setCantidad(2);
    }

    @Test
    void testFindAll() {
        when(carritoRepository.findAll()).thenReturn(List.of(carrito));

        List<CarritoResponseDto> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        assertEquals(2L, resultado.get(0).getProducto().getId());
        assertEquals(2, resultado.get(0).getCantidad());
        verify(carritoRepository).findAll();
    }

    @Test
    void testFindById() {
        when(carritoRepository.findById(3L)).thenReturn(Optional.of(carrito));

        Optional<CarritoResponseDto> resultado = carritoService.findById(3L);

        assertTrue(resultado.isPresent());
        assertEquals(3L, resultado.get().getId());
        assertEquals(1L, resultado.get().getUsuario().getId());
        assertEquals(2L, resultado.get().getProducto().getId());
        verify(carritoRepository).findById(3L);
    }

    @Test
    void testSave() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoResponseDto resultado = carritoService.save(carritoRequestDto);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals(1L, resultado.getUsuario().getId());
        assertEquals(2L, resultado.getProducto().getId());
        assertEquals(2, resultado.getCantidad());
        verify(usuarioRepository).findById(1L);
        verify(productoRepository).findById(2L);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(carritoRepository).deleteById(3L);

        carritoService.deleteById(3L);

        verify(carritoRepository).deleteById(3L);
    }

    @Test
    void testFindByUsuarioId() {
        when(carritoRepository.findByUsuarioId(1L)).thenReturn(List.of(carrito));

        List<CarritoResponseDto> resultado = carritoService.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        assertEquals(2L, resultado.get(0).getProducto().getId());
        verify(carritoRepository).findByUsuarioId(1L);
    }
}