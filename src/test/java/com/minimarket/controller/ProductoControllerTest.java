package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.ProductoRequestDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class})
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testActualizarProductoPermiteAdministrador() throws Exception {
        ProductoRequestDto request = new ProductoRequestDto(2L, "Arroz integral", 1800.0, 15, 1L);
        ProductoResponseDto response = new ProductoResponseDto(
                2L,
                "Arroz integral",
                1800.0,
                15,
                new CategoriaResponseDto(1L, "Abarrotes")
        );

        when(productoService.findById(2L)).thenReturn(Optional.of(response));
        when(productoService.save(any(ProductoRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/productos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("Arroz integral"));

        verify(productoService).findById(2L);
        verify(productoService).save(any(ProductoRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testActualizarProductoRechazaEmpleado() throws Exception {
        ProductoRequestDto request = new ProductoRequestDto(2L, "Arroz integral", 1800.0, 15, 1L);

        mockMvc.perform(put("/api/productos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productoService);
    }

    @Test
    void testActualizarProductoRequiereAutenticacion() throws Exception {
        ProductoRequestDto request = new ProductoRequestDto(2L, "Arroz integral", 1800.0, 15, 1L);

        mockMvc.perform(put("/api/productos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(productoService, never()).findById(2L);
        verify(productoService, never()).save(any(ProductoRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testActualizarProductoRetornaNotFoundSiNoExiste() throws Exception {
        ProductoRequestDto request = new ProductoRequestDto(99L, "Arroz integral", 1800.0, 15, 1L);

        when(productoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(productoService).findById(99L);
        verify(productoService, never()).save(any(ProductoRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testEliminarProductoPermiteAdministradorSiExiste() throws Exception {
        when(productoService.findById(2L)).thenReturn(Optional.of(
                new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes"))
        ));
        doNothing().when(productoService).deleteById(2L);

        mockMvc.perform(delete("/api/productos/2"))
                .andExpect(status().isNoContent());

        verify(productoService).findById(2L);
        verify(productoService).deleteById(2L);
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testEliminarProductoRetornaNotFoundSiNoExiste() throws Exception {
        when(productoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());

        verify(productoService).findById(99L);
        verify(productoService, never()).deleteById(99L);
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testObtenerProductoPorIdRetornaOkSiExiste() throws Exception {
        when(productoService.findById(2L)).thenReturn(Optional.of(
                new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes"))
        ));

        mockMvc.perform(get("/api/productos/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Arroz"));

        verify(productoService).findById(2L);
    }
}