package com.minimarket.controller.assembler;

import com.minimarket.controller.CategoriaController;
import com.minimarket.dto.CategoriaResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaResponseDto, EntityModel<CategoriaResponseDto>> {

    @Override
    public EntityModel<CategoriaResponseDto> toModel(CategoriaResponseDto categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withRel("categorias"),
                linkTo(methodOn(CategoriaController.class).actualizarCategoria(categoria.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(CategoriaController.class).eliminarCategoria(categoria.getId())).withRel("eliminar")
        );
    }

    public CollectionModel<EntityModel<CategoriaResponseDto>> toCollectionModel(List<CategoriaResponseDto> categorias) {
        return CollectionModel.of(categorias.stream().map(this::toModel).toList(),
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withSelfRel());
    }
}