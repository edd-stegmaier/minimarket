package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.dto.RolResponseDto;
import com.minimarket.dto.UsuarioResponseDto;
import com.minimarket.exception.GlobalExceptionHandler;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(authorities = "ADMINISTRADOR")
    void testListarUsuariosPermiteAdministrador() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(
                new UsuarioResponseDto(1L, "administrador", Set.of(new RolResponseDto(1L, "ADMINISTRADOR")))
        ));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("administrador"))
                .andExpect(jsonPath("$[0].roles[0].nombre").value("ADMINISTRADOR"));

        verify(usuarioService).findAll();
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testListarUsuariosRechazaEmpleado() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(usuarioService);
    }

    @Test
    void testListarUsuariosRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized());

        verify(usuarioService, never()).findAll();
    }

        @Test
        @WithMockUser(authorities = "ADMINISTRADOR")
        void testActualizarUsuarioPermiteAdministradorSiExiste() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(
            new UsuarioResponseDto(1L, "administrador", Set.of(new RolResponseDto(1L, "ADMINISTRADOR")))
        ));
        when(usuarioService.save(any())).thenReturn(
            new UsuarioResponseDto(1L, "admin-actualizado", Set.of(new RolResponseDto(1L, "ADMINISTRADOR")))
        );

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new com.minimarket.dto.UsuarioRequestDto(
                    1L,
                    "admin-actualizado",
                    "Password123",
                    Set.of(1L)
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("admin-actualizado"));

        verify(usuarioService).findById(1L);
        verify(usuarioService).save(any());
        }

        @Test
        @WithMockUser(authorities = "ADMINISTRADOR")
        void testActualizarUsuarioRetornaNotFoundSiNoExiste() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/99")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new com.minimarket.dto.UsuarioRequestDto(
                    99L,
                    "usuario999",
                    "Password123",
                    Set.of(1L)
                ))))
            .andExpect(status().isNotFound());

        verify(usuarioService).findById(99L);
        verify(usuarioService, never()).save(any());
        }

        @Test
        @WithMockUser(authorities = "ADMINISTRADOR")
        void testEliminarUsuarioPermiteAdministradorSiExiste() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(
            new UsuarioResponseDto(1L, "administrador", Set.of(new RolResponseDto(1L, "ADMINISTRADOR")))
        ));
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
            .andExpect(status().isNoContent());

        verify(usuarioService).findById(1L);
        verify(usuarioService).deleteById(1L);
        }

        @Test
        @WithMockUser(authorities = "ADMINISTRADOR")
        void testEliminarUsuarioRetornaNotFoundSiNoExiste() throws Exception {
        when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/usuarios/99"))
            .andExpect(status().isNotFound());

        verify(usuarioService).findById(99L);
        verify(usuarioService, never()).deleteById(99L);
        }

        @Test
        @WithMockUser(authorities = "ADMINISTRADOR")
        void testObtenerUsuarioPorIdRetornaOkSiExiste() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(Optional.of(
            new UsuarioResponseDto(1L, "administrador", Set.of(new RolResponseDto(1L, "ADMINISTRADOR")))
        ));

        mockMvc.perform(get("/api/usuarios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("administrador"));

        verify(usuarioService).findById(1L);
        }
}