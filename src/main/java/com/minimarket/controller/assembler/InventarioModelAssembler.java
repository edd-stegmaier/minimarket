package com.minimarket.controller.assembler;

import com.minimarket.controller.InventarioController;
import com.minimarket.dto.InventarioResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<InventarioResponseDto, EntityModel<InventarioResponseDto>> {

    @Override
    public EntityModel<InventarioResponseDto> toModel(InventarioResponseDto inventario) {
        return EntityModel.of(inventario,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withRel("inventario"),
                linkTo(methodOn(InventarioController.class).actualizarMovimiento(inventario.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(InventarioController.class).eliminarMovimiento(inventario.getId())).withRel("eliminar")
        );
    }

    public CollectionModel<EntityModel<InventarioResponseDto>> toCollectionModel(List<InventarioResponseDto> movimientos) {
        return CollectionModel.of(movimientos.stream().map(this::toModel).toList(),
                linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario()).withSelfRel());
    }
}