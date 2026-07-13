package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.controller.assembler.VentaModelAssembler;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.DetalleVentaRequestDto;
import com.minimarket.dto.DetalleVentaResponseDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.dto.RolResponseDto;
import com.minimarket.dto.UsuarioResponseDto;
import com.minimarket.dto.VentaRequestDto;
import com.minimarket.dto.VentaResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class, VentaModelAssembler.class})
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

        @MockitoBean
    private VentaService ventaService;

        @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testGuardarVentaPermiteCajeroYReflejaProductosVendidos() throws Exception {
        VentaRequestDto request = new VentaRequestDto(
                1L,
                1L,
                new Date(0L),
                List.of(new DetalleVentaRequestDto(1L, 1L, 2L, 2, 1500.0))
        );
        VentaResponseDto response = new VentaResponseDto(
                1L,
                new UsuarioResponseDto(1L, "cajero", Set.of(new RolResponseDto(2L, "EMPLEADO"))),
                new Date(0L),
                List.of(new DetalleVentaResponseDto(
                        1L,
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2,
                        1500.0
                ))
        );

        when(ventaService.save(any(VentaRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/ventas/1"))
                .andExpect(jsonPath("$.usuario.username").value("cajero"))
                .andExpect(jsonPath("$.detalles[0].producto.nombre").value("Arroz"))
                .andExpect(jsonPath("$.detalles[0].cantidad").value(2))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(ventaService).save(any(VentaRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testGuardarVentaRechazaCliente() throws Exception {
        VentaRequestDto request = new VentaRequestDto(
                1L,
                1L,
                new Date(0L),
                List.of(new DetalleVentaRequestDto(1L, 1L, 2L, 2, 1500.0))
        );

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(ventaService);
    }

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testGuardarVentaRechazaAdministrador() throws Exception {
        VentaRequestDto request = new VentaRequestDto(
                1L,
                1L,
                new Date(0L),
                List.of(new DetalleVentaRequestDto(1L, 1L, 2L, 2, 1500.0))
        );

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(ventaService);
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testListarVentasRetornaOk() throws Exception {
        when(ventaService.findAll()).thenReturn(List.of(
                new VentaResponseDto(
                        1L,
                        new UsuarioResponseDto(1L, "cajero", Set.of(new RolResponseDto(2L, "EMPLEADO"))),
                        new Date(0L),
                        List.of()
                )
        ));

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ventaResponseDtoList[0].usuario.username").value("cajero"))
                .andExpect(jsonPath("$._embedded.ventaResponseDtoList[0]._links.self.href").exists());

        verify(ventaService).findAll();
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testObtenerVentaPorIdRetornaOkSiExiste() throws Exception {
        when(ventaService.findById(1L)).thenReturn(Optional.of(
                new VentaResponseDto(
                        1L,
                        new UsuarioResponseDto(1L, "cajero", Set.of(new RolResponseDto(2L, "EMPLEADO"))),
                        new Date(0L),
                        List.of(new DetalleVentaResponseDto(
                                1L,
                                new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                                2,
                                1500.0
                        ))
                )
        ));

        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detalles[0].producto.nombre").value("Arroz"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(ventaService).findById(1L);
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testObtenerVentaPorIdRetornaNotFoundSiNoExiste() throws Exception {
        when(ventaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound());

        verify(ventaService).findById(99L);
    }
}