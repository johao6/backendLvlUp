package com.levelup.backend.controllers;

import com.levelup.backend.models.Categoria;
import com.levelup.backend.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    @GetMapping
    @Operation(summary = "Obtener todas las categorías activas")
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.getCategoriaById(id));
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria createdCategoria = categoriaService.createCategoria(categoria);
        return new ResponseEntity<>(createdCategoria, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría")
    public ResponseEntity<Categoria> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.updateCategoria(id, categoria));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría (desactivar)")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
