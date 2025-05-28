package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.model.Usuario;
import com.projetoCortesias.cortesias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getUsuarioLogado(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        return ResponseEntity.ok(Map.of(
            "email", usuario.getEmail(),
            "permissoes", usuario.getPermissoes()
        ));
    }
}
