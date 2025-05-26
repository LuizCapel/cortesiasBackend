package com.projetoCortesias.cortesias.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventoDTO {
    private String nome;
    private LocalDate data;
    private String local;
    private String responsavel;
    private int quantidadeCortesias;

    // Getters e setters
}
