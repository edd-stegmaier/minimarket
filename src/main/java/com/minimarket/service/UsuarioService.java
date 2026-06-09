package com.minimarket.service;

import com.minimarket.dto.UsuarioRequestDto;
import com.minimarket.dto.UsuarioResponseDto;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioResponseDto> findAll();
    Optional<UsuarioResponseDto> findById(Long id);
    Optional<UsuarioResponseDto> findByUsername(String username);
    UsuarioResponseDto save(UsuarioRequestDto usuario);
    void deleteById(Long id);
}