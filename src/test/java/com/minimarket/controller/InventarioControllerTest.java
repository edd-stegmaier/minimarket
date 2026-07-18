package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.controller.assembler.InventarioModelAssembler;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.InventarioRequestDto;
import com.minimarket.dto.InventarioResponseDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
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

@WebMvcTest(InventarioController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class, InventarioModelAssembler.class})
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

        @MockitoBean
    private InventarioService inventarioService;

        @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testRegistrarEntradaPermiteUsuarioConPermiso() throws Exception {
        InventarioRequestDto request = new InventarioRequestDto(1L, 2L, 10, "ENTRADA", new Date(0L));
        InventarioResponseDto response = new InventarioResponseDto(
                1L,
                new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                10,
                "ENTRADA",
                new Date(0L)
        );

        when(inventarioService.save(any(InventarioRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/api/inventario/1"))
                .andExpect(jsonPath("$.tipoMovimiento").value("ENTRADA"))
            .andExpect(jsonPath("$.cantidad").value(10))
            .andExpect(jsonPath("$._links.self.href").exists());

        verify(inventarioService).save(any(InventarioRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testRegistrarSalidaPermiteAdministrador() throws Exception {
        InventarioRequestDto request = new InventarioRequestDto(2L, 2L, 4, "SALIDA", new Date(0L));
        InventarioResponseDto response = new InventarioResponseDto(
                2L,
                new ProductoResponseDto(2L, "Arroz", 1500.0, 16, new CategoriaResponseDto(1L, "Abarrotes")),
                4,
                "SALIDA",
                new Date(0L)
        );

        when(inventarioService.save(any(InventarioRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/api/inventario/2"))
                .andExpect(jsonPath("$.tipoMovimiento").value("SALIDA"))
            .andExpect(jsonPath("$.cantidad").value(4))
            .andExpect(jsonPath("$._links.self.href").exists());

        verify(inventarioService).save(any(InventarioRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testRegistrarMovimientoRechazaUsuarioSinPermiso() throws Exception {
        InventarioRequestDto request = new InventarioRequestDto(1L, 2L, 10, "ENTRADA", new Date(0L));

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(inventarioService);
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testActualizarMovimientoPermiteAdministradorSiExiste() throws Exception {
    InventarioRequestDto request = new InventarioRequestDto(2L, 2L, 4, "SALIDA", new Date(0L));
    InventarioResponseDto response = new InventarioResponseDto(
        2L,
        new ProductoResponseDto(2L, "Arroz", 1500.0, 16, new CategoriaResponseDto(1L, "Abarrotes")),
        4,
        "SALIDA",
        new Date(0L)
    );

    when(inventarioService.findById(2L)).thenReturn(Optional.of(response));
    when(inventarioService.save(any(InventarioRequestDto.class))).thenReturn(response);

    mockMvc.perform(put("/api/inventario/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tipoMovimiento").value("SALIDA"))
        .andExpect(jsonPath("$._links.self.href").exists());

    verify(inventarioService).findById(2L);
    verify(inventarioService).save(any(InventarioRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testActualizarMovimientoRetornaNotFoundSiNoExiste() throws Exception {
    InventarioRequestDto request = new InventarioRequestDto(99L, 2L, 4, "SALIDA", new Date(0L));

    when(inventarioService.findById(99L)).thenReturn(Optional.empty());

    mockMvc.perform(put("/api/inventario/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(inventarioService).findById(99L);
    verify(inventarioService, never()).save(any(InventarioRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testEliminarMovimientoPermiteAdministradorSiExiste() throws Exception {
    when(inventarioService.findById(2L)).thenReturn(Optional.of(
        new InventarioResponseDto(
            2L,
            new ProductoResponseDto(2L, "Arroz", 1500.0, 16, new CategoriaResponseDto(1L, "Abarrotes")),
            4,
            "SALIDA",
            new Date(0L)
        )
    ));
    doNothing().when(inventarioService).deleteById(2L);

    mockMvc.perform(delete("/api/inventario/2"))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

    verify(inventarioService).findById(2L);
    verify(inventarioService).deleteById(2L);
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testEliminarMovimientoRetornaNotFoundSiNoExiste() throws Exception {
    when(inventarioService.findById(99L)).thenReturn(Optional.empty());

    mockMvc.perform(delete("/api/inventario/99"))
        .andExpect(status().isNotFound());

    verify(inventarioService).findById(99L);
    verify(inventarioService, never()).deleteById(99L);
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testObtenerMovimientoPorIdRetornaOkSiExiste() throws Exception {
    when(inventarioService.findById(2L)).thenReturn(Optional.of(
        new InventarioResponseDto(
            2L,
            new ProductoResponseDto(2L, "Arroz", 1500.0, 16, new CategoriaResponseDto(1L, "Abarrotes")),
            4,
            "SALIDA",
            new Date(0L)
        )
    ));

    mockMvc.perform(get("/api/inventario/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tipoMovimiento").value("SALIDA"))
        .andExpect(jsonPath("$._links.self.href").exists());

    verify(inventarioService).findById(2L);
    }
}