package com.minimarket.dto;

import com.minimarket.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapper {

    private Mapper() {
    }

    public static Usuario toEntity(UsuarioRequestDto dto, Set<Rol> roles) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setRoles(roles);
        return usuario;
    }

    public static Rol toEntity(RolRequestDto dto) {
        Rol rol = new Rol();
        rol.setId(dto.getId());
        rol.setNombre(dto.getNombre());
        return rol;
    }

    public static Categoria toEntity(CategoriaRequestDto dto) {
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        return categoria;
    }

    public static Producto toEntity(ProductoRequestDto dto, Categoria categoria) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);
        return producto;
    }

    public static Inventario toEntity(InventarioRequestDto dto, Producto producto) {
        Inventario inventario = new Inventario();
        inventario.setId(dto.getId());
        inventario.setProducto(producto);
        inventario.setCantidad(dto.getCantidad());
        inventario.setTipoMovimiento(dto.getTipoMovimiento());
        inventario.setFechaMovimiento(dto.getFechaMovimiento());
        return inventario;
    }

    public static Carrito toEntity(CarritoRequestDto dto, Usuario usuario, Producto producto) {
        Carrito carrito = new Carrito();
        carrito.setId(dto.getId());
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(dto.getCantidad());
        return carrito;
    }

    public static Venta toEntity(VentaRequestDto dto, Usuario usuario, List<DetalleVenta> detalles) {
        Venta venta = new Venta();
        venta.setId(dto.getId());
        venta.setUsuario(usuario);
        venta.setFecha(dto.getFecha());
        venta.setDetalles(detalles);
        return venta;
    }

    public static DetalleVenta toEntity(DetalleVentaRequestDto dto, Venta venta, Producto producto) {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(dto.getId());
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(dto.getCantidad());
        detalleVenta.setPrecio(dto.getPrecio());
        return detalleVenta;
    }

    public static UsuarioResponseDto toDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());

        Set<RolResponseDto> roles = usuario.getRoles() == null
                ? Collections.emptySet()
                : usuario.getRoles().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toSet());

        dto.setRoles(roles);
        return dto;
    }

    public static RolResponseDto toDto(Rol rol) {
        RolResponseDto dto = new RolResponseDto();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());

        return dto;
    }

    public static CategoriaResponseDto toDto(Categoria categoria) {
        CategoriaResponseDto dto = new CategoriaResponseDto();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }

    public static ProductoResponseDto toDto(Producto producto) {
        ProductoResponseDto dto = new ProductoResponseDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoria(producto.getCategoria() == null ? null : toDto(producto.getCategoria()));
        return dto;
    }

    public static InventarioResponseDto toDto(Inventario inventario) {
        InventarioResponseDto dto = new InventarioResponseDto();
        dto.setId(inventario.getId());
        dto.setProducto(inventario.getProducto() == null ? null : toDto(inventario.getProducto()));
        dto.setCantidad(inventario.getCantidad());
        dto.setTipoMovimiento(inventario.getTipoMovimiento());
        dto.setFechaMovimiento(inventario.getFechaMovimiento());
        return dto;
    }

    public static CarritoResponseDto toDto(Carrito carrito) {
        CarritoResponseDto dto = new CarritoResponseDto();
        dto.setId(carrito.getId());
        dto.setUsuario(carrito.getUsuario() == null ? null : toDto(carrito.getUsuario()));
        dto.setProducto(carrito.getProducto() == null ? null : toDto(carrito.getProducto()));
        dto.setCantidad(carrito.getCantidad());
        return dto;
    }

    public static DetalleVentaResponseDto toDto(DetalleVenta detalleVenta) {
        DetalleVentaResponseDto dto = new DetalleVentaResponseDto();
        dto.setId(detalleVenta.getId());
        dto.setProducto(detalleVenta.getProducto() == null ? null : toDto(detalleVenta.getProducto()));
        dto.setCantidad(detalleVenta.getCantidad());
        dto.setPrecio(detalleVenta.getPrecio());
        return dto;
    }

    public static VentaResponseDto toDto(Venta venta) {
        VentaResponseDto dto = new VentaResponseDto();
        dto.setId(venta.getId());
        dto.setUsuario(venta.getUsuario() == null ? null : toDto(venta.getUsuario()));
        dto.setFecha(venta.getFecha());
        List<DetalleVentaResponseDto> detalles = venta.getDetalles() == null
                ? Collections.emptyList()
                : venta.getDetalles().stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
        dto.setDetalles(detalles);
        return dto;
    }

}
