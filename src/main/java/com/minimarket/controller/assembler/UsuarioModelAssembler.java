package com.minimarket.controller.assembler;

import com.minimarket.controller.UsuarioController;
import com.minimarket.dto.UsuarioResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponseDto, EntityModel<UsuarioResponseDto>> {

    @Override
    public EntityModel<UsuarioResponseDto> toModel(UsuarioResponseDto usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).actualizarUsuario(usuario.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(UsuarioController.class).eliminarUsuario(usuario.getId())).withRel("eliminar")
        );
    }

    public CollectionModel<EntityModel<UsuarioResponseDto>> toCollectionModel(List<UsuarioResponseDto> usuarios) {
        return CollectionModel.of(usuarios.stream().map(this::toModel).toList(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());
    }
}