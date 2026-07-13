package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.controller.assembler.CategoriaModelAssembler;
import com.minimarket.dto.CategoriaRequestDto;
import com.minimarket.dto.CategoriaResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.CategoriaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class, CategoriaModelAssembler.class})
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testListarCategoriasPermiteCliente() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of(new CategoriaResponseDto(1L, "Abarrotes")));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.categoriaResponseDtoList[0].nombre").value("Abarrotes"))
            .andExpect(jsonPath("$._embedded.categoriaResponseDtoList[0]._links.self.href").exists())
            .andExpect(jsonPath("$._links.self.href").exists());

        verify(categoriaService).findAll();
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testObtenerCategoriaPorIdRetornaOkSiExiste() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(Optional.of(new CategoriaResponseDto(1L, "Abarrotes")));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Abarrotes"))
            .andExpect(jsonPath("$._links.self.href").exists());

        verify(categoriaService).findById(1L);
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testActualizarCategoriaPermiteEmpleadoSiExiste() throws Exception {
        CategoriaRequestDto request = new CategoriaRequestDto(1L, "Limpieza");

        when(categoriaService.findById(1L)).thenReturn(Optional.of(new CategoriaResponseDto(1L, "Abarrotes")));
        when(categoriaService.save(any(CategoriaRequestDto.class))).thenReturn(new CategoriaResponseDto(1L, "Limpieza"));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Limpieza"))
            .andExpect(jsonPath("$._links.self.href").exists());

        verify(categoriaService).findById(1L);
        verify(categoriaService).save(any(CategoriaRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testActualizarCategoriaRechazaCliente() throws Exception {
        CategoriaRequestDto request = new CategoriaRequestDto(1L, "Limpieza");

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(categoriaService);
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testActualizarCategoriaRetornaNotFoundSiNoExiste() throws Exception {
        CategoriaRequestDto request = new CategoriaRequestDto(99L, "Limpieza");

        when(categoriaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categorias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(categoriaService).findById(99L);
        verify(categoriaService, never()).save(any(CategoriaRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testEliminarCategoriaPermiteEmpleadoSiExiste() throws Exception {
        when(categoriaService.findById(1L)).thenReturn(Optional.of(new CategoriaResponseDto(1L, "Abarrotes")));
        doNothing().when(categoriaService).deleteById(1L);

        mockMvc.perform(delete("/api/categorias/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("Categoria eliminada exitosamente"))
            .andExpect(jsonPath("$._links.categorias.href").exists());

        verify(categoriaService).findById(1L);
        verify(categoriaService).deleteById(1L);
    }

    @Test
    void testListarCategoriasRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isUnauthorized());

        verify(categoriaService, never()).findAll();
    }
}