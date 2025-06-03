package com.projetoCortesias.cortesias.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class EventoDTO {
    private String nome;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dataFim;

    private String local;
    private String responsavel;
    private int quantidadeCortesias;

}
