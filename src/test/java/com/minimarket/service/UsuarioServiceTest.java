package com.minimarket.service;

import com.minimarket.dto.UsuarioRequestDto;
import com.minimarket.dto.UsuarioResponseDto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private RolRepository rolRepository;

	@InjectMocks
	private UsuarioService usuarioService;

	private Rol rol;
	private Usuario usuario;
	private UsuarioRequestDto usuarioRequestDto;

	@BeforeEach
	void setUp() {
		rol = new Rol();
		rol.setId(1L);
		rol.setNombre("ADMIN");

		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setUsername("juan");
		usuario.setPassword("encoded12345678");
		usuario.setRoles(Set.of(rol));

		usuarioRequestDto = new UsuarioRequestDto();
		usuarioRequestDto.setId(1L);
		usuarioRequestDto.setUsername("juan");
		usuarioRequestDto.setPassword("12345678");
		usuarioRequestDto.setRoleIds(Set.of(1L));
	}

	@Test
	void testFindAll() {
		when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

		List<UsuarioResponseDto> resultado = usuarioService.findAll();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals(1L, resultado.get(0).getId());
		assertEquals("juan", resultado.get(0).getUsername());
		assertEquals(1, resultado.get(0).getRoles().size());
		assertEquals("ADMIN", resultado.get(0).getRoles().iterator().next().getNombre());
		verify(usuarioRepository).findAll();
	}

	@Test
	void testFindById() {
		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

		var resultado = usuarioService.findById(1L);

		assertTrue(resultado.isPresent());
		assertEquals(1L, resultado.get().getId());
		assertEquals("juan", resultado.get().getUsername());
		verify(usuarioRepository).findById(1L);
	}

	@Test
	void testFindByUsername() {
		when(usuarioRepository.findByUsername("juan")).thenReturn(Optional.of(usuario));

		var resultado = usuarioService.findByUsername("juan");

		assertTrue(resultado.isPresent());
		assertEquals(1L, resultado.get().getId());
		assertEquals("juan", resultado.get().getUsername());
		verify(usuarioRepository).findByUsername("juan");
	}

	@Test
	void testSave() {
		when(rolRepository.findAllById(Set.of(1L))).thenReturn(List.of(rol));
		when(passwordEncoder.encode("12345678")).thenReturn("encoded12345678");
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		UsuarioResponseDto resultado = usuarioService.save(usuarioRequestDto);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("juan", resultado.getUsername());
		assertEquals(1, resultado.getRoles().size());
		assertEquals("ADMIN", resultado.getRoles().iterator().next().getNombre());

		ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(usuarioCaptor.capture());
		assertEquals("encoded12345678", usuarioCaptor.getValue().getPassword());
		assertEquals("juan", usuarioCaptor.getValue().getUsername());

		verify(rolRepository).findAllById(Set.of(1L));
		verify(passwordEncoder).encode("12345678");
	}

	@Test
	void testDeleteById() {
		doNothing().when(usuarioRepository).deleteById(1L);

		usuarioService.deleteById(1L);

		verify(usuarioRepository).deleteById(1L);
	}
}
