package com.projetoCortesias.cortesias.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaDTO {
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String cidade;
    private String telefone;
    private String email;

    // Getters e setters
}
