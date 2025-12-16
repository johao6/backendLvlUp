package com.levelup.backend.services;

import com.levelup.backend.dto.PedidoRequest;
import com.levelup.backend.dto.PedidoResponse;
import com.levelup.backend.exceptions.BadRequestException;
import com.levelup.backend.exceptions.ResourceNotFoundException;
import com.levelup.backend.models.DetallePedido;
import com.levelup.backend.models.Pedido;
import com.levelup.backend.models.Producto;
import com.levelup.backend.models.Usuario;
import com.levelup.backend.repositories.PedidoRepository;
import com.levelup.backend.repositories.ProductoRepository;
import com.levelup.backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    private PedidoResponse convertirAPedidoResponse(Pedido pedido) {
        PedidoResponse.UsuarioBasicoDto usuarioDto = new PedidoResponse.UsuarioBasicoDto(
                pedido.getUsuario().getId(),
                pedido.getUsuario().getNombre(),
                pedido.getUsuario().getEmail()
        );

        List<PedidoResponse.DetallePedidoDto> detallesDto = pedido.getDetalles().stream()
                .map(detalle -> {
                    PedidoResponse.ProductoBasicoDto productoDto = new PedidoResponse.ProductoBasicoDto(
                            detalle.getProducto().getId(),
                            detalle.getProducto().getNombre(),
                            detalle.getProducto().getImagen(),
                            detalle.getProducto().getPrecio()
                    );

                    return new PedidoResponse.DetallePedidoDto(
                            detalle.getId(),
                            productoDto,
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario(),
                            detalle.getSubtotal()
                    );
                })
                .collect(Collectors.toList());

        return new PedidoResponse(
                pedido.getId(),
                usuarioDto,
                pedido.getTotal(),
                pedido.getEstado().toString(),
                pedido.getDireccionEnvio(),
                pedido.getNotas(),
                detallesDto,
                pedido.getFechaCreacion()
        );
    }

    //ANTES DE CAMBIO
    //public List<Pedido> getAllPedidos() {return pedidoRepository.findAll();}

    public List<PedidoResponse> getAllPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertirAPedidoResponse)
                .collect(Collectors.toList());
    }
    //ANTES
    /*public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
    }*/

    // DESPUÉS
    public PedidoResponse getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
        return convertirAPedidoResponse(pedido);
    }

    //ANTES
    /*public List<Pedido> getPedidosByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }*/

    // DESPUÉS
    public List<PedidoResponse> getPedidosByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirAPedidoResponse)
                .collect(Collectors.toList());
    }
    //ANTES
    /*@Transactional
    public Pedido createPedido(PedidoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));
        
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setNotas(request.getNotas());
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (PedidoRequest.ItemCarrito item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", item.getProductoId()));
            
            if (producto.getStock() < item.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            
            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.calcularSubtotal();
            
            pedido.addDetalle(detalle);
            total = total.add(detalle.getSubtotal());
            
            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }*/

    //DESPUES
    @Transactional
    public PedidoResponse createPedido(PedidoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getUsuarioId()));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setNotas(request.getNotas());
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        BigDecimal total = BigDecimal.ZERO;

        for (PedidoRequest.ItemCarrito item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.calcularSubtotal();

            pedido.addDetalle(detalle);
            total = total.add(detalle.getSubtotal());

            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return convertirAPedidoResponse(pedidoGuardado);
    }

    //ANTES
    /*@Transactional
    public Pedido updateEstadoPedido(Long id, Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = getPedidoById(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }*/

    // DESPUÉS
    @Transactional
    public PedidoResponse updateEstadoPedido(Long id, Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
        pedido.setEstado(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return convertirAPedidoResponse(pedidoActualizado);
    }
}
