package com.projetoCortesias.cortesias.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CortesiaDTO {
    private String codigo;
    private boolean resgatada;
    private String pessoaNome;
    private String pessoaCpf;
    private LocalDate dataSolicitacao;

}
