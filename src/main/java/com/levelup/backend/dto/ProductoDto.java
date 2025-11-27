package com.levelup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioAnterior;
    private Integer stock;
    private String imagen;
    private String marca;
    private String categoria;
    private Boolean destacado;
    private Integer cantidadEnCarrito;
}

