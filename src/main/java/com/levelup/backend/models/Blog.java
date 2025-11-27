package com.levelup.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El título es requerido")
    @Size(min = 5, max = 255, message = "El título debe tener entre 5 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String titulo;
    
    @NotBlank(message = "El contenido es requerido")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @Size(max = 500, message = "El resumen no puede exceder 500 caracteres")
    @Column(length = 500)
    private String resumen;
    
    @Column(length = 255)
    private String imagen;
    
    @Column(length = 100)
    private String autor;
    
    @Column(nullable = false)
    private Boolean publicado = false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
}
