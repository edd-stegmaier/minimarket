package com.minimarket.service;

import com.minimarket.dto.Mapper;
import com.minimarket.dto.UsuarioRequestDto;
import com.minimarket.dto.UsuarioResponseDto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.util.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RolRepository rolRepository;

	public List<UsuarioResponseDto> findAll() {
		return usuarioRepository.findAll().stream()
				.map(Mapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<UsuarioResponseDto> findById(Long id) {
		return usuarioRepository.findById(Objects.requireNonNull(id, "id")).map(Mapper::toDto);
	}

	public Optional<UsuarioResponseDto> findByUsername(String username) {
		return usuarioRepository.findByUsername(username).map(Mapper::toDto);
	}

	public UsuarioResponseDto save(UsuarioRequestDto UsuarioDto) {
		UsuarioDto.setUsername(Sanitizer.sanitizeInput(UsuarioDto.getUsername()));
		Set<Long> roleIds = Objects.requireNonNull(UsuarioDto.getRoleIds(), "roleIds");
		Set<Rol> roles = rolRepository.findAllById(roleIds).stream()
				.collect(Collectors.toSet());

		if (roles.size() != roleIds.size()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno o mas roles no existen");
		}

		Usuario usuario = Mapper.toEntity(UsuarioDto, roles);
		if (usuario.getPassword() != null && !isBcryptHash(usuario.getPassword())) {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		return Mapper.toDto(Objects.requireNonNull(usuarioRepository.save(Objects.requireNonNull(usuario))));
	}

	public void deleteById(Long id) {
		usuarioRepository.deleteById(Objects.requireNonNull(id, "id"));
	}

	private boolean isBcryptHash(String password) {
		return password.matches("\\$2[aby]\\$.{56}");
	}
}