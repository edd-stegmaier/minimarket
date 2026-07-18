package com.minimarket.controller.assembler;

import com.minimarket.controller.CarritoController;
import com.minimarket.controller.ProductoController;
import com.minimarket.controller.UsuarioController;
import com.minimarket.dto.CarritoResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<CarritoResponseDto, EntityModel<CarritoResponseDto>> {

    @Override
    public EntityModel<CarritoResponseDto> toModel(CarritoResponseDto carrito) {
        EntityModel<CarritoResponseDto> model = EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(carrito.getId())).withSelfRel(),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito"),
                linkTo(methodOn(CarritoController.class).actualizarCarrito(carrito.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(CarritoController.class).eliminarProductoDelCarrito(carrito.getId())).withRel("eliminar")
        );

        if (carrito.getUsuario() != null && carrito.getUsuario().getId() != null) {
            model.add(linkTo(methodOn(UsuarioController.class)
                    .obtenerUsuarioPorId(carrito.getUsuario().getId())).withRel("usuario"));
        }

        if (carrito.getProducto() != null && carrito.getProducto().getId() != null) {
            model.add(linkTo(methodOn(ProductoController.class)
                    .obtenerProductoPorId(carrito.getProducto().getId())).withRel("producto"));
        }

        return model;
    }

    public CollectionModel<EntityModel<CarritoResponseDto>> toCollectionModel(List<CarritoResponseDto> carritos) {
        return CollectionModel.of(carritos.stream().map(this::toModel).toList(),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withSelfRel());
    }
}