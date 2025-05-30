package com.projetoCortesias.cortesias.dto;

import com.projetoCortesias.cortesias.model.Permissao;
import java.util.List;

public class UsuarioRequest {
    private String email;
    private String senha;
    private String nome;
    private List<Permissao> permissoes;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Permissao> getPermissoes() { return permissoes; }
    public void setPermissoes(List<Permissao> permissoes) { this.permissoes = permissoes; }
}