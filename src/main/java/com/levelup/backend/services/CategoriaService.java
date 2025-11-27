package com.levelup.backend.services;

import com.levelup.backend.exceptions.ResourceNotFoundException;
import com.levelup.backend.models.Categoria;
import com.levelup.backend.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findByActivoTrue();
    }
    
    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
    }
    
    @Transactional
    public Categoria createCategoria(Categoria categoria) {
        categoria.setActivo(true);
        return categoriaRepository.save(categoria);
    }
    
    @Transactional
    public Categoria updateCategoria(Long id, Categoria categoriaDetails) {
        Categoria categoria = getCategoriaById(id);
        
        categoria.setNombre(categoriaDetails.getNombre());
        categoria.setDescripcion(categoriaDetails.getDescripcion());
        categoria.setImagen(categoriaDetails.getImagen());
        
        return categoriaRepository.save(categoria);
    }
    
    @Transactional
    public void deleteCategoria(Long id) {
        Categoria categoria = getCategoriaById(id);
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }
}
