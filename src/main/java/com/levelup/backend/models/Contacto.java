package com.levelup.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contactos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "Debe ser un email válido")
    @Column(nullable = false, length = 150)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    @NotBlank(message = "El asunto es requerido")
    @Size(min = 5, max = 200, message = "El asunto debe tener entre 5 y 200 caracteres")
    @Column(nullable = false, length = 200)
    private String asunto;
    
    @NotBlank(message = "El mensaje es requerido")
    @Size(min = 10, message = "El mensaje debe tener al menos 10 caracteres")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;
    
    @Column(nullable = false)
    private Boolean leido = false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
}
