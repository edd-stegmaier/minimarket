package com.minimarket.service;

import com.minimarket.repository.*;
import com.minimarket.entity.*;
import com.minimarket.dto.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
    
    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private VentaService ventaService;

    private Usuario usuario;
    private Producto producto;
    private Venta venta;
    private DetalleVenta detalleVenta;

    private VentaRequestDto ventaRequestDto;
    private DetalleVentaRequestDto detalleVentaRequestDto;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("Juan");

        producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Arroz");

        venta = new Venta();
        venta.setId(3L);
        venta.setUsuario(usuario);

        detalleVenta = new DetalleVenta();
        detalleVenta.setId(4L);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(10);
        detalleVenta.setPrecio(1000.0);

        venta.setDetalles(List.of(detalleVenta));

        // se crean DTOs equivalentes a entidades
        ventaRequestDto = new VentaRequestDto();
        ventaRequestDto.setId(3L);
        ventaRequestDto.setUsuarioId(1L);

        detalleVentaRequestDto = new DetalleVentaRequestDto();
        detalleVentaRequestDto.setId(4L);
        detalleVentaRequestDto.setVentaId(3L);
        detalleVentaRequestDto.setProductoId(2L);
        detalleVentaRequestDto.setCantidad(10);
        detalleVentaRequestDto.setPrecio(1000.0);

        ventaRequestDto.setDetalles(List.of(detalleVentaRequestDto));
    }

    @Test
    void testFindAll() {
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<VentaResponseDto> resultado = ventaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        assertEquals(4L, resultado.get(0).getDetalles().get(0).getId());
        verify(ventaRepository).findAll();
    }

    @Test
    void testFindDyId() {
        when(ventaRepository.findById(3L)).thenReturn(java.util.Optional.of(venta));

        var resultado = ventaService.findById(3L);

        assertTrue(resultado.isPresent());
        assertEquals(3L, resultado.get().getId());
        verify(ventaRepository).findById(3L);
    }

    @Test
    void testSave() {

        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(usuario));
        when(productoRepository.findById(2L)).thenReturn(java.util.Optional.of(producto));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        VentaResponseDto resultado = ventaService.save(ventaRequestDto);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals(1L, resultado.getUsuario().getId());
        assertEquals(4L, resultado.getDetalles().get(0).getId());
        verify(usuarioRepository).findById(1L);
        verify(productoRepository).findById(2L);
        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    void testFindByUsuarioId() {
        when(ventaRepository.findByUsuarioId(1L)).thenReturn(List.of(venta));

        List<VentaResponseDto> resultado = ventaService.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        verify(ventaRepository).findByUsuarioId(1L);
    }
    
}
