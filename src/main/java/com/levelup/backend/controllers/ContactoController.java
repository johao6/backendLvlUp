package com.levelup.backend.controllers;

import com.levelup.backend.models.Contacto;
import com.levelup.backend.services.ContactoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Contacto", description = "Gestión de mensajes de contacto")
public class ContactoController {
    
    private final ContactoService contactoService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los mensajes de contacto")
    public ResponseEntity<List<Contacto>> getAllContactos() {
        return ResponseEntity.ok(contactoService.getAllContactos());
    }
    
    @GetMapping("/no-leidos")
    @Operation(summary = "Obtener mensajes no leídos")
    public ResponseEntity<List<Contacto>> getContactosNoLeidos() {
        return ResponseEntity.ok(contactoService.getContactosNoLeidos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener mensaje por ID")
    public ResponseEntity<Contacto> getContactoById(@PathVariable Long id) {
        return ResponseEntity.ok(contactoService.getContactoById(id));
    }
    
    @PostMapping
    @Operation(summary = "Enviar mensaje de contacto")
    public ResponseEntity<Contacto> createContacto(@Valid @RequestBody Contacto contacto) {
        Contacto createdContacto = contactoService.createContacto(contacto);
        return new ResponseEntity<>(createdContacto, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/leido")
    @Operation(summary = "Marcar mensaje como leído")
    public ResponseEntity<Contacto> marcarComoLeido(@PathVariable Long id) {
        return ResponseEntity.ok(contactoService.marcarComoLeido(id));
    }
}
