package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.controller.assembler.DetalleVentaModelAssembler;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.DetalleVentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetalleVentaController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class, DetalleVentaModelAssembler.class})
class DetalleVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DetalleVentaService detalleVentaService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testListarDetalleVentasPermiteEmpleado() throws Exception {
        when(detalleVentaService.findAll()).thenReturn(List.of(
                new DetalleVentaResponseDto(
                        1L,
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2,
                        1500.0
                )
        ));

        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.detalleVentaResponseDtoList[0].producto.nombre").value("Arroz"))
                .andExpect(jsonPath("$._embedded.detalleVentaResponseDtoList[0]._links.self.href").exists());

        verify(detalleVentaService).findAll();
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testListarDetalleVentasRechazaCliente() throws Exception {
        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(detalleVentaService);
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testObtenerDetalleVentaPorIdRetornaOkSiExiste() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(Optional.of(
                new DetalleVentaResponseDto(
                        1L,
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2,
                        1500.0
                )
        ));

        mockMvc.perform(get("/api/detalle-ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(2))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(detalleVentaService).findById(1L);
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testGuardarDetalleVentaPermiteEmpleado() throws Exception {
        DetalleVentaRequestDto request = new DetalleVentaRequestDto(1L, 1L, 2L, 2, 1500.0);
        DetalleVentaResponseDto response = new DetalleVentaResponseDto(
                1L,
                new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                2,
                1500.0
        );

        when(detalleVentaService.save(any(DetalleVentaRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/detalle-ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/detalle-ventas/1"))
                .andExpect(jsonPath("$.precio").value(1500.0))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(detalleVentaService).save(any(DetalleVentaRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testActualizarDetalleVentaRetornaNotFoundSiNoExiste() throws Exception {
        DetalleVentaRequestDto request = new DetalleVentaRequestDto(99L, 1L, 2L, 2, 1500.0);

        when(detalleVentaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/detalle-ventas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(detalleVentaService).findById(99L);
        verify(detalleVentaService, never()).save(any(DetalleVentaRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testEliminarDetalleVentaPermiteEmpleadoSiExiste() throws Exception {
        when(detalleVentaService.findById(1L)).thenReturn(Optional.of(
                new DetalleVentaResponseDto(
                        1L,
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2,
                        1500.0
                )
        ));
        doNothing().when(detalleVentaService).deleteById(1L);

        mockMvc.perform(delete("/api/detalle-ventas/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(detalleVentaService).findById(1L);
        verify(detalleVentaService).deleteById(1L);
    }

    @Test
    void testListarDetalleVentasRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/api/detalle-ventas"))
                .andExpect(status().isUnauthorized());

        verify(detalleVentaService, never()).findAll();
    }
}