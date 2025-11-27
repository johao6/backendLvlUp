package com.levelup.backend.repositories;

import com.levelup.backend.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByDestacadoTrueAndActivoTrue();
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Producto> searchByKeyword(String keyword);
}
