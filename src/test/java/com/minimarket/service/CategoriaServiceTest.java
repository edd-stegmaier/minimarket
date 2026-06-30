package com.minimarket.service;

import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;

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
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaRequestDto categoriaRequestDto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        categoriaRequestDto = new CategoriaRequestDto();
        categoriaRequestDto.setId(1L);
        categoriaRequestDto.setNombre("Bebidas");
    }

    @Test
    void testFindAll() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponseDto> resultado = categoriaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Bebidas", resultado.get(0).getNombre());
        verify(categoriaRepository).findAll();
    }

    @Test
    void testFindById() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        var resultado = categoriaService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Bebidas", resultado.get().getNombre());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    void testSave() {
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDto resultado = categoriaService.save(categoriaRequestDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Bebidas", resultado.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.deleteById(1L);

        verify(categoriaRepository).deleteById(1L);
    }
}