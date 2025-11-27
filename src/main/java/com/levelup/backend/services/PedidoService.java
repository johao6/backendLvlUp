package com.levelup.backend.services;

import com.levelup.backend.dto.PedidoRequest;
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

@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }
    
    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
    }
    
    public List<Pedido> getPedidosByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
    
    @Transactional
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
    }
    
    @Transactional
    public Pedido updateEstadoPedido(Long id, Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = getPedidoById(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
