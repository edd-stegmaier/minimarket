package com.minimarket.controller.assembler;

import com.minimarket.controller.ProductoController;
import com.minimarket.dto.ProductoResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoResponseDto, EntityModel<ProductoResponseDto>> {

    @Override
    public EntityModel<ProductoResponseDto> toModel(ProductoResponseDto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"),
                linkTo(methodOn(ProductoController.class).actualizarProducto(producto.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("eliminar")
        );
    }

    public CollectionModel<EntityModel<ProductoResponseDto>> toCollectionModel(List<ProductoResponseDto> productos) {
        List<EntityModel<ProductoResponseDto>> items = productos.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(items,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }
}