package com.levelup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    private Long usuarioId;
    private String direccionEnvio;
    private String notas;
    private List<ItemCarrito> items;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemCarrito {
        private Long productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}
