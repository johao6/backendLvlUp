package com.levelup.backend.controllers;

import com.levelup.backend.models.Producto;
import com.levelup.backend.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductoController {
    
    private final ProductoService productoService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los productos activos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }
    
    @GetMapping("/destacados")
    @Operation(summary = "Obtener productos destacados")
    public ResponseEntity<List<Producto>> getProductosDestacados() {
        return ResponseEntity.ok(productoService.getProductosDestacados());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }
    
    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Obtener productos por categoría")
    public ResponseEntity<List<Producto>> getProductosByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.getProductosByCategoria(categoriaId));
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por palabra clave")
    public ResponseEntity<List<Producto>> searchProductos(@RequestParam String keyword) {
        return ResponseEntity.ok(productoService.searchProductos(keyword));
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<Producto> createProducto(@Valid @RequestBody Producto producto) {
        Producto createdProducto = productoService.createProducto(producto);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<Producto> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.updateProducto(id, producto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (desactivar)")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
