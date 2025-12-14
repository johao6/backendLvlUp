package com.levelup.backend.controllers;
import com.levelup.backend.dto.CarritoItemDto;
import com.levelup.backend.dto.ProductoDto;
import com.levelup.backend.services.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Carrito", description = "Gestion del carrito de compras")
public class CarritoController {
    private final CarritoService carritoService;
    @GetMapping("/api/carrito/{usuarioId}")
    @Operation(summary = "Obtener carrito del usuario")
    public ResponseEntity<List<CarritoItemDto>> obtenerCarrito(@PathVariable Long usuarioId) {//CAMBIAR A PRODUCTODTO
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }
    @PostMapping("/api/carrito/{usuarioId}/{productoId}")
    @Operation(summary = "Agregar producto al carrito")
    public ResponseEntity<Void> agregarAlCarrito(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        carritoService.agregarAlCarrito(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/api/carrito/{usuarioId}/{productoId}")
    @Operation(summary = "Quitar producto del carrito (reduce cantidad o elimina)")
    public ResponseEntity<Void> quitarDelCarrito(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        carritoService.quitarDelCarrito(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/api/carritovacio/{usuarioId}")
    @Operation(summary = "Vaciar carrito del usuario")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/carrito/increase/{usuarioId}/{productoId}")
    @Operation(summary = "Aumentar cantidad de un producto en el carrito")
    public ResponseEntity<Void> aumentarCantidad(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        carritoService.aumentarCantidad(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/carrito/decrease/{usuarioId}/{productoId}")
    @Operation(summary = "Disminuir cantidad de un producto en el carrito")
    public ResponseEntity<Void> disminuirCantidad(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        carritoService.disminuirCantidad(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }
}