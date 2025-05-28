
package com.projetoCortesias.cortesias.service;

import com.projetoCortesias.cortesias.model.Usuario;
import com.projetoCortesias.cortesias.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new User(u.getEmail(), u.getSenha(),
            u.getPermissoes().stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.name()))
             .collect(Collectors.toSet()));
    }
}
