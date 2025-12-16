package com.levelup.backend.controllers;

import com.levelup.backend.dto.PedidoRequest;
import com.levelup.backend.dto.PedidoResponse;
import com.levelup.backend.models.Pedido;
import com.levelup.backend.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión de pedidos y carrito")
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public ResponseEntity<List<PedidoResponse>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<PedidoResponse> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedidoById(id));
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pedidos de un usuario")
    public ResponseEntity<List<PedidoResponse>> getPedidosByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.getPedidosByUsuario(usuarioId));
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo pedido")
    public ResponseEntity<PedidoResponse> createPedido(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse createdPedido = pedidoService.createPedido(request);
        return new ResponseEntity<>(createdPedido, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido")
    public ResponseEntity<PedidoResponse> updateEstadoPedido(
            @PathVariable Long id,
            @RequestParam Pedido.EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.updateEstadoPedido(id, estado));
    }
}
