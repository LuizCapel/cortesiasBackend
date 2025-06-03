
package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.model.Usuario;
import com.projetoCortesias.cortesias.repository.UsuarioRepository;
import com.projetoCortesias.cortesias.security.JwtUtil;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getSenha()));
        Usuario usuario = usuarioRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<String> permissoes = usuario.getPermissoes().stream()
                .map(Enum::name) // ou .map(Permissao::name)
                .toList();

        String token = jwtUtil.generateToken(usuario.getEmail(), permissoes);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario u) {
        u.setSenha(encoder.encode(u.getSenha()));
        return ResponseEntity.ok(repo.save(u));
    }

    @Getter @Setter
    static class LoginRequest {
        private String email;
        private String senha;
    }

    @Getter @AllArgsConstructor
    static class TokenResponse {
        private String token;
    }
}
