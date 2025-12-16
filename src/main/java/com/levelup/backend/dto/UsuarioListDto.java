package com.levelup.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioListDto {
    private String nombre;
    private String email;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}
