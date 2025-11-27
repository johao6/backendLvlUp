package com.levelup.backend.controllers;

import com.levelup.backend.models.Blog;
import com.levelup.backend.services.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Blogs", description = "Gestión de blogs")
public class BlogController {
    
    private final BlogService blogService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los blogs publicados")
    public ResponseEntity<List<Blog>> getPublishedBlogs() {
        return ResponseEntity.ok(blogService.getPublishedBlogs());
    }
    
    @GetMapping("/all")
    @Operation(summary = "Obtener todos los blogs (incluye no publicados)")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener blog por ID")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo blog")
    public ResponseEntity<Blog> createBlog(@Valid @RequestBody Blog blog) {
        Blog createdBlog = blogService.createBlog(blog);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar blog")
    public ResponseEntity<Blog> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody Blog blog) {
        return ResponseEntity.ok(blogService.updateBlog(id, blog));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar blog")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
}
