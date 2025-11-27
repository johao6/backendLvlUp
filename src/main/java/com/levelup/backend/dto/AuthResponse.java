package com.levelup.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private String token;
    private String tipo;
    private Long id;
    private String nombre;
    private String email;
    private List<String> roles;
    
    // Constructor simple para login (solo token, id, nombre)
    public AuthResponse(String token, Long id, String nombre) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
    }
    
    // Constructor completo para otras respuestas
    public AuthResponse(String token, Long id, String nombre, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.roles = roles;
    }
}
