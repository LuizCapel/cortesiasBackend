package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.dto.UsuarioRequest;
import com.projetoCortesias.cortesias.model.Usuario;
import com.projetoCortesias.cortesias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    @GetMapping
    public List<Usuario> listar(@RequestParam(required = false) String nome,
                                @RequestParam(required = false) String email) {
        return usuarioRepository.findAll().stream()
                .filter(u -> (nome == null || u.getNome().toLowerCase().contains(nome.toLowerCase())))
                .filter(u -> (email == null || u.getEmail().toLowerCase().contains(email.toLowerCase())))
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody UsuarioRequest req) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario u = opt.get();
        u.setNome(req.getNome());
        u.setEmail(req.getEmail());
        u.setPermissoes(new HashSet<>(req.getPermissoes()));

        if (req.getSenha() != null && !req.getSenha().isBlank()) {
            u.setSenha(passwordEncoder.encode(req.getSenha()));
        }

        usuarioRepository.save(u);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) return ResponseEntity.notFound().build();
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
