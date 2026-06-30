package com.minimarket.service;

import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;

import com.minimarket.entity.*;
import com.minimarket.dto.InventarioResponseDto;
import com.minimarket.dto.InventarioRequestDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Producto producto;
    private Inventario inventario;
    private InventarioRequestDto inventarioRequestDto;
    
    @BeforeEach
    void setUp() {
        Date fecha = new java.util.Date();

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
    
        inventario = new Inventario();
        inventario.setId(10L);
        inventario.setProducto(producto);
        inventario.setCantidad(10);
        inventario.setTipoMovimiento("INGRESO");
        inventario.setFechaMovimiento(fecha);

        //mismo inventario como request DTO
        inventarioRequestDto = new InventarioRequestDto();
        inventarioRequestDto.setProductoId(1L);
        inventarioRequestDto.setCantidad(10);
        inventarioRequestDto.setTipoMovimiento("INGRESO");
        inventarioRequestDto.setFechaMovimiento(fecha);
    }

    @Test
    void testFindAll() {

        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        List<InventarioResponseDto> resultado = inventarioService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getProducto().getId());
        assertEquals("Arroz", resultado.get(0).getProducto().getNombre());
        assertEquals(10, resultado.get(0).getCantidad());
        assertEquals("INGRESO", resultado.get(0).getTipoMovimiento());
        verify(inventarioRepository).findAll();
    }

    @Test
    void testFindById() {

        when(inventarioRepository.findById(10L)).thenReturn(java.util.Optional.of(inventario));

        var resultado = inventarioService.findById(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
        assertEquals(1L, resultado.get().getProducto().getId());
        assertEquals("Arroz", resultado.get().getProducto().getNombre());
        assertEquals(10, resultado.get().getCantidad());
        assertEquals("INGRESO", resultado.get().getTipoMovimiento());
        verify(inventarioRepository).findById(10L);
    }

    @Test
    void testSave(){
    
        when(productoRepository.findById(1L)).thenReturn(java.util.Optional.of(producto));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        InventarioResponseDto resultado = inventarioService.save(inventarioRequestDto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(1L, resultado.getProducto().getId());
        assertEquals(10, resultado.getCantidad());
        assertEquals("INGRESO", resultado.getTipoMovimiento());
        verify(productoRepository).findById(1L);
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void testDeleteById() {
        
        doNothing().when(inventarioRepository).deleteById(10L);

        inventarioService.deleteById(10L);

        verify(inventarioRepository).deleteById(10L);
    }

    @Test
    void testFindByProductoId() {

        when(inventarioRepository.findByProductoId(1L)).thenReturn(List.of(inventario));

        List<InventarioResponseDto> resultado = inventarioService.findByProductoId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getId());
        assertEquals(1L, resultado.get(0).getProducto().getId());
        assertEquals("Arroz", resultado.get(0).getProducto().getNombre());
        assertEquals(10, resultado.get(0).getCantidad());
        assertEquals("INGRESO", resultado.get(0).getTipoMovimiento());
        verify(inventarioRepository).findByProductoId(1L);
    }
}
