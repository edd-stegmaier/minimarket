package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.controller.assembler.CarritoModelAssembler;
import com.minimarket.dto.CarritoRequestDto;
import com.minimarket.dto.CarritoResponseDto;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.dto.ProductoResponseDto;
import com.minimarket.dto.RolResponseDto;
import com.minimarket.dto.UsuarioResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.CarritoService;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarritoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class, CarritoModelAssembler.class})
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarritoService carritoService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testListarCarritoPermiteCliente() throws Exception {
        when(carritoService.findAll()).thenReturn(List.of(
                new CarritoResponseDto(
                        1L,
                        new UsuarioResponseDto(1L, "cliente", Set.of(new RolResponseDto(1L, "CLIENTE"))),
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2
                )
        ));

        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.carritoResponseDtoList[0].usuario.username").value("cliente"))
                .andExpect(jsonPath("$._embedded.carritoResponseDtoList[0].producto.nombre").value("Arroz"))
                .andExpect(jsonPath("$._embedded.carritoResponseDtoList[0]._links.self.href").exists());

        verify(carritoService).findAll();
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testObtenerCarritoPorIdRetornaOkSiExiste() throws Exception {
        when(carritoService.findById(1L)).thenReturn(Optional.of(
                new CarritoResponseDto(
                        1L,
                        new UsuarioResponseDto(1L, "cliente", Set.of(new RolResponseDto(1L, "CLIENTE"))),
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2
                )
        ));

        mockMvc.perform(get("/api/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(2))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(carritoService).findById(1L);
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testActualizarCarritoPermiteClienteSiExiste() throws Exception {
        CarritoRequestDto request = new CarritoRequestDto(1L, 1L, 2L, 3);
        CarritoResponseDto response = new CarritoResponseDto(
                1L,
                new UsuarioResponseDto(1L, "cliente", Set.of(new RolResponseDto(1L, "CLIENTE"))),
                new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                3
        );

        when(carritoService.findById(1L)).thenReturn(Optional.of(response));
        when(carritoService.save(any(CarritoRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/carrito/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(3))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(carritoService).findById(1L);
        verify(carritoService).save(any(CarritoRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testActualizarCarritoRetornaNotFoundSiNoExiste() throws Exception {
        CarritoRequestDto request = new CarritoRequestDto(99L, 1L, 2L, 3);

        when(carritoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/carrito/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(carritoService).findById(99L);
        verify(carritoService, never()).save(any(CarritoRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testEliminarProductoDelCarritoPermiteSiExiste() throws Exception {
        when(carritoService.findById(1L)).thenReturn(Optional.of(
                new CarritoResponseDto(
                        1L,
                        new UsuarioResponseDto(1L, "cliente", Set.of(new RolResponseDto(1L, "CLIENTE"))),
                        new ProductoResponseDto(2L, "Arroz", 1500.0, 20, new CategoriaResponseDto(1L, "Abarrotes")),
                        2
                )
        ));
        doNothing().when(carritoService).deleteById(1L);

        mockMvc.perform(delete("/api/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Producto eliminado del carrito exitosamente"))
                .andExpect(jsonPath("$._links.carrito.href").exists());

        verify(carritoService).findById(1L);
        verify(carritoService).deleteById(1L);
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testEliminarProductoDelCarritoRetornaNotFoundSiNoExiste() throws Exception {
        when(carritoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/carrito/99"))
                .andExpect(status().isNotFound());

        verify(carritoService).findById(99L);
        verify(carritoService, never()).deleteById(99L);
    }

    @Test
    void testListarCarritoRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/api/carrito"))
                .andExpect(status().isUnauthorized());

        verify(carritoService, never()).findAll();
    }
}