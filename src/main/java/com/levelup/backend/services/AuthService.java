package com.levelup.backend.services;

import com.levelup.backend.dto.AuthResponse;
import com.levelup.backend.dto.LoginRequest;
import com.levelup.backend.dto.RegistroRequest;
import com.levelup.backend.exceptions.BadRequestException;
import com.levelup.backend.models.Usuario;
import com.levelup.backend.repositories.UsuarioRepository;
import com.levelup.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse registro(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        // Guardar contraseña encriptada (BCrypt)
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setActivo(true);

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        usuario.setRoles(roles);

        usuario = usuarioRepository.save(usuario);

        // Generar token basado en UserDetails (para consistencia)
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList())
        );

        String token = jwtTokenProvider.generateToken(userDetails);

        return new AuthResponse(token, usuario.getId(), usuario.getNombre(), usuario.getEmail(), new ArrayList<>(usuario.getRoles()));
    }

    public AuthResponse login(LoginRequest request) {
        //PARA VERIFICAR ELIMINAR POSTERIORMENTE
        System.out.println("🟩 [Service] Intentando autenticar...");
        System.out.println("🟩 [Service] Email: " + request.getEmail());
        System.out.println("🟩 [Service] Password: " + request.getPassword());

        // Autenticación mediante AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        //PARA VERIFICAR ELIMINAR POSTERIORMENTE
        System.out.println("🟩 [Service] Autenticación exitosa para: " + request.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtener UserDetails y generar token con roles
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        // Obtener usuario completo para devolver id/nombre/email (opcional)
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.startsWith("ROLE_") ? s.substring(5) : s) // convertir "ROLE_ADMIN" -> "ADMIN"
                .collect(Collectors.toList());

        return new AuthResponse(token, usuario.getId(), usuario.getNombre(), usuario.getEmail(), roles);
    }

}
