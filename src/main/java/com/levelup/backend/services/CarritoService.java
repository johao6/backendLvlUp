package com.levelup.backend.services;

import com.levelup.backend.dto.ProductoDto;
import com.levelup.backend.exceptions.BadRequestException;
import com.levelup.backend.models.ItemCarrito;
import com.levelup.backend.models.Producto;
import com.levelup.backend.models.Usuario;
import com.levelup.backend.repositories.ItemCarritoRepository;
import com.levelup.backend.repositories.ProductoRepository;
import com.levelup.backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final ItemCarritoRepository itemCarritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public List<ProductoDto> obtenerCarrito(Long usuarioId) {
        List<ItemCarrito> items = itemCarritoRepository.findByUsuarioId(usuarioId);

        return items.stream()
                .map(this::convertirAProductoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void agregarAlCarrito(Long usuarioId, Long productoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new BadRequestException("Producto no encontrado"));

        if (!producto.getActivo()) {
            throw new BadRequestException("El producto no está disponible");
        }

        if (producto.getStock() <= 0) {
            throw new BadRequestException("Producto sin stock disponible");
        }

        // Verificar si el producto ya está en el carrito
        var itemExistente = itemCarritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId);

        if (itemExistente.isPresent()) {
            // Incrementar cantidad
            ItemCarrito item = itemExistente.get();
            if (item.getCantidad() >= producto.getStock()) {
                throw new BadRequestException("No hay suficiente stock disponible");
            }
            item.setCantidad(item.getCantidad() + 1);
            itemCarritoRepository.save(item);
        } else {
            // Agregar nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setUsuario(usuario);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(1);
            itemCarritoRepository.save(nuevoItem);
        }
    }

    @Transactional
    public void quitarDelCarrito(Long usuarioId, Long productoId) {
        var itemExistente = itemCarritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElseThrow(() -> new BadRequestException("El producto no está en el carrito"));

        if (itemExistente.getCantidad() > 1) {
            // Decrementar cantidad
            itemExistente.setCantidad(itemExistente.getCantidad() - 1);
            itemCarritoRepository.save(itemExistente);
        } else {
            // Eliminar item
            itemCarritoRepository.delete(itemExistente);
        }
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        itemCarritoRepository.deleteByUsuarioId(usuarioId);
    }

    @Transactional
    public void aumentarCantidad(Long usuarioId, Long productoId) {
        var itemExistente = itemCarritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElseThrow(() -> new BadRequestException("El producto no está en el carrito"));

        Producto producto = itemExistente.getProducto();

        if (!producto.getActivo()) {
            throw new BadRequestException("El producto no está disponible");
        }

        if (itemExistente.getCantidad() >= producto.getStock()) {
            throw new BadRequestException("No hay suficiente stock disponible");
        }

        itemExistente.setCantidad(itemExistente.getCantidad() + 1);
        itemCarritoRepository.save(itemExistente);
    }

    @Transactional
    public void disminuirCantidad(Long usuarioId, Long productoId) {
        var itemExistente = itemCarritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .orElseThrow(() -> new BadRequestException("El producto no está en el carrito"));

        if (itemExistente.getCantidad() > 1) {
            itemExistente.setCantidad(itemExistente.getCantidad() - 1);
            itemCarritoRepository.save(itemExistente);
        } else {
            itemCarritoRepository.delete(itemExistente);
        }
    }

    private ProductoDto convertirAProductoDto(ItemCarrito item) {
        Producto p = item.getProducto();
        ProductoDto dto = new ProductoDto();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setPrecioAnterior(p.getPrecioAnterior());
        dto.setStock(p.getStock());
        dto.setImagen(p.getImagen());
        dto.setMarca(p.getMarca());
        dto.setCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null);
        dto.setDestacado(p.getDestacado());
        dto.setCantidadEnCarrito(item.getCantidad());
        return dto;
    }
}

