package com.minimarket.service;

import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.VentaRepository;

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
public class DetalleVentaServiceTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private DetalleVentaService detalleVentaService;

    private Venta venta;
    private Producto producto;
    private DetalleVenta detalleVenta;
    private DetalleVentaRequestDto detalleVentaRequestDto;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(3L);

        producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Arroz");

        detalleVenta = new DetalleVenta();
        detalleVenta.setId(4L);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(10);
        detalleVenta.setPrecio(1000.0);

        detalleVentaRequestDto = new DetalleVentaRequestDto();
        detalleVentaRequestDto.setId(4L);
        detalleVentaRequestDto.setVentaId(3L);
        detalleVentaRequestDto.setProductoId(2L);
        detalleVentaRequestDto.setCantidad(10);
        detalleVentaRequestDto.setPrecio(1000.0);
    }

    @Test
    void testFindAll() {
        when(detalleVentaRepository.findAll()).thenReturn(List.of(detalleVenta));

        List<DetalleVentaResponseDto> resultado = detalleVentaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(4L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(0).getProducto().getId());
        assertEquals("Arroz", resultado.get(0).getProducto().getNombre());
        assertEquals(10, resultado.get(0).getCantidad());
        assertEquals(1000.0, resultado.get(0).getPrecio());
        verify(detalleVentaRepository).findAll();
    }

    @Test
    void testFindById() {
        when(detalleVentaRepository.findById(4L)).thenReturn(Optional.of(detalleVenta));

        var resultado = detalleVentaService.findById(4L);

        assertTrue(resultado.isPresent());
        assertEquals(4L, resultado.get().getId());
        assertEquals(2L, resultado.get().getProducto().getId());
        verify(detalleVentaRepository).findById(4L);
    }

    @Test
    void testSave() {
        when(ventaRepository.findById(3L)).thenReturn(Optional.of(venta));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        when(detalleVentaRepository.save(any(DetalleVenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DetalleVentaResponseDto resultado = detalleVentaService.save(detalleVentaRequestDto);

        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals(2L, resultado.getProducto().getId());
        assertEquals("Arroz", resultado.getProducto().getNombre());
        assertEquals(10, resultado.getCantidad());
        assertEquals(1000.0, resultado.getPrecio());
        verify(ventaRepository).findById(3L);
        verify(productoRepository).findById(2L);
        verify(detalleVentaRepository).save(any(DetalleVenta.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(detalleVentaRepository).deleteById(4L);

        detalleVentaService.deleteById(4L);

        verify(detalleVentaRepository).deleteById(4L);
    }

    @Test
    void testFindByVentaId() {
        when(detalleVentaRepository.findByVentaId(3L)).thenReturn(List.of(detalleVenta));

        List<DetalleVentaResponseDto> resultado = detalleVentaService.findByVentaId(3L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(4L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(0).getProducto().getId());
        verify(detalleVentaRepository).findByVentaId(3L);
    }
}