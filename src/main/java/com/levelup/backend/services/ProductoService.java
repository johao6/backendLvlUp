package com.levelup.backend.services;

import com.levelup.backend.exceptions.ResourceNotFoundException;
import com.levelup.backend.models.Producto;
import com.levelup.backend.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    public List<Producto> getAllProductos() {
        return productoRepository.findByActivoTrue();
    }
    
    public List<Producto> getProductosDestacados() {
        return productoRepository.findByDestacadoTrueAndActivoTrue();
    }
    
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    }
    
    public List<Producto> getProductosByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }
    
    public List<Producto> searchProductos(String keyword) {
        return productoRepository.searchByKeyword(keyword);
    }
    
    @Transactional
    public Producto createProducto(Producto producto) {
        producto.setActivo(true);
        return productoRepository.save(producto);
    }
    
    @Transactional
    public Producto updateProducto(Long id, Producto productoDetails) {
        Producto producto = getProductoById(id);
        
        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setPrecioAnterior(productoDetails.getPrecioAnterior());
        producto.setStock(productoDetails.getStock());
        producto.setImagen(productoDetails.getImagen());
        producto.setMarca(productoDetails.getMarca());
        producto.setSku(productoDetails.getSku());
        producto.setDestacado(productoDetails.getDestacado());
        producto.setCategoria(productoDetails.getCategoria());
        
        return productoRepository.save(producto);
    }
    
    @Transactional
    public void deleteProducto(Long id) {
        Producto producto = getProductoById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
