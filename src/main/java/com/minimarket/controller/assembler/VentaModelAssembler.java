package com.minimarket.controller.assembler;

import com.minimarket.controller.DetalleVentaController;
import com.minimarket.controller.UsuarioController;
import com.minimarket.controller.VentaController;
import com.minimarket.dto.DetalleVentaResponseDto;
import com.minimarket.dto.VentaResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaResponseDto, EntityModel<VentaResponseDto>> {

    @Override
    public EntityModel<VentaResponseDto> toModel(VentaResponseDto venta) {
        EntityModel<VentaResponseDto> model = EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).obtenerVentaPorId(venta.getId())).withSelfRel(),
                linkTo(methodOn(VentaController.class).listarVentas()).withRel("ventas")
        );

        if (venta.getUsuario() != null && venta.getUsuario().getId() != null) {
            model.add(linkTo(methodOn(UsuarioController.class)
                    .obtenerUsuarioPorId(venta.getUsuario().getId())).withRel("usuario"));
        }

        if (venta.getDetalles() != null) {
            for (DetalleVentaResponseDto detalle : venta.getDetalles()) {
                if (detalle != null && detalle.getId() != null) {
                    model.add(linkTo(methodOn(DetalleVentaController.class)
                            .obtenerDetalleVentaPorId(detalle.getId())).withRel("detalleVenta"));
                }
            }
        }

        return model;
    }

    public CollectionModel<EntityModel<VentaResponseDto>> toCollectionModel(List<VentaResponseDto> ventas) {
        return CollectionModel.of(ventas.stream().map(this::toModel).toList(),
                linkTo(methodOn(VentaController.class).listarVentas()).withSelfRel());
    }
}