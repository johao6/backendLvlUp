package com.levelup.backend.controllers;

import com.levelup.backend.dto.UsuarioListDto;
import com.levelup.backend.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Listado de usuarios registrados en la pagina")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioListDto>> listarUsuarios() {
        List<UsuarioListDto> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/usuario/{email}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String email) {
        usuarioService.eliminarPorEmail(email);
        return ResponseEntity.noContent().build();
    }

}
