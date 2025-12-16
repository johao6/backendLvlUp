package com.levelup.backend.services;

import com.levelup.backend.dto.UsuarioListDto;
import com.levelup.backend.models.Usuario;
import com.levelup.backend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioListDto> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioListDto dto = new UsuarioListDto();
            dto.setNombre(usuario.getNombre());
            dto.setEmail(usuario.getEmail());
            dto.setFechaCreacion(usuario.getFechaCreacion());
            dto.setActivo(usuario.getActivo());
            return dto;
        }).collect(Collectors.toList());
    }

    public void eliminarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

}
