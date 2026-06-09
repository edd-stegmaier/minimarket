package com.minimarket.service.impl;

import com.minimarket.dto.UsuarioResponseDto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.util.Sanitizer;
import com.minimarket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.minimarket.dto.Mapper;
import com.minimarket.dto.UsuarioRequestDto;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<UsuarioResponseDto> findAll() {
        return usuarioRepository.findAll().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioResponseDto> findById(Long id) {
        return usuarioRepository.findById(id).map(Mapper::toDto);
    }

    @Override
    public Optional<UsuarioResponseDto> findByUsername(String username) {
        return usuarioRepository.findByUsername(username).map(Mapper::toDto);
    }

    @Override
    public UsuarioResponseDto save(UsuarioRequestDto UsuarioDto) {
        UsuarioDto.setUsername(Sanitizer.sanitizeInput(UsuarioDto.getUsername()));
        Set<Rol> roles = rolRepository.findAllById(UsuarioDto.getRoleIds()).stream()
                .collect(Collectors.toSet());

        if (roles.size() != UsuarioDto.getRoleIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno o mas roles no existen");
        }

        Usuario usuario = Mapper.toEntity(UsuarioDto, roles);
        if (usuario.getPassword() != null && !isBcryptHash(usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return Mapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    private boolean isBcryptHash(String password) {
        return password.matches("\\$2[aby]\\$.{56}");
    }
}
