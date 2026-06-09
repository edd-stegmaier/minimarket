package com.minimarket.security.config;

import com.minimarket.dto.UsuarioRequestDto;
import com.minimarket.entity.Rol;
import com.minimarket.repository.RolRepository;
import com.minimarket.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class SecurityDataInitializer {

    @Bean
    public CommandLineRunner seedSecurityData(RolRepository rolRepository, UsuarioService usuarioService) {
        return args -> {
            Rol cliente = ensureRole(rolRepository, "ROL_CLIENTE");
            Rol empleado = ensureRole(rolRepository, "ROL_EMPLEADO");
            Rol gerente = ensureRole(rolRepository, "ROL_GERENTE");

            ensureUser(usuarioService, "cliente", "Cliente123!", cliente);
            ensureUser(usuarioService, "empleado", "Empleado123!", empleado);
            ensureUser(usuarioService, "gerente", "Gerente123!", gerente);
        };
    }

    private Rol ensureRole(RolRepository rolRepository, String nombreRol) {
        return rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setNombre(nombreRol);
                    return rolRepository.save(rol);
                });
    }

    private void ensureUser(UsuarioService usuarioService, String username, String password, Rol rol) {
        if (usuarioService.findByUsername(username).isPresent()) {
            return;
        }

        UsuarioRequestDto usuario = new UsuarioRequestDto();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setRoleIds(Set.of(rol).stream().map(Rol::getId).collect(Collectors.toSet()));
        usuarioService.save(usuario);
    }
}