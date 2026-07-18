package com.minimarket.controller.assembler;

import com.minimarket.controller.DetalleVentaController;
import com.minimarket.controller.ProductoController;
import com.minimarket.dto.DetalleVentaResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVentaResponseDto, EntityModel<DetalleVentaResponseDto>> {

    @Override
    public EntityModel<DetalleVentaResponseDto> toModel(DetalleVentaResponseDto detalleVenta) {
        EntityModel<DetalleVentaResponseDto> model = EntityModel.of(detalleVenta,
                linkTo(methodOn(DetalleVentaController.class).obtenerDetalleVentaPorId(detalleVenta.getId())).withSelfRel(),
                linkTo(methodOn(DetalleVentaController.class).listarDetalleVentas()).withRel("detalleVentas"),
                linkTo(methodOn(DetalleVentaController.class).actualizarDetalleVenta(detalleVenta.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(DetalleVentaController.class).eliminarDetalleVenta(detalleVenta.getId())).withRel("eliminar")
        );

        if (detalleVenta.getProducto() != null && detalleVenta.getProducto().getId() != null) {
            model.add(linkTo(methodOn(ProductoController.class)
                    .obtenerProductoPorId(detalleVenta.getProducto().getId())).withRel("producto"));
        }

        return model;
    }

    public CollectionModel<EntityModel<DetalleVentaResponseDto>> toCollectionModel(List<DetalleVentaResponseDto> detalleVentas) {
        return CollectionModel.of(detalleVentas.stream().map(this::toModel).toList(),
                linkTo(methodOn(DetalleVentaController.class).listarDetalleVentas()).withSelfRel());
    }
}