package com.projetoCortesias.cortesias.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class EventoDTO {
    private String nome;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date dataInicio;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date dataFim;

    private String local;
    private String responsavel;
    private int quantidadeCortesias;

    // Getters e setters
}
