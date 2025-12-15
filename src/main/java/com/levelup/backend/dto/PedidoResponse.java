package com.levelup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private UsuarioBasicoDto usuario;
    private BigDecimal total;
    private String estado;
    private String direccionEnvio;
    private String notas;
    private List<DetallePedidoDto> detalles;
    private LocalDateTime fechaCreacion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioBasicoDto {
        private Long id;
        private String nombre;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetallePedidoDto {
        private Long id;
        private ProductoBasicoDto producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoBasicoDto {
        private Long id;
        private String nombre;
        private String imagen;
        private BigDecimal precio;
    }
}
