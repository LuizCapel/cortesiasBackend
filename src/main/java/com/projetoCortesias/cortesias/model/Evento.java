package com.projetoCortesias.cortesias.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private String local;
    private String responsavel;
    private Integer quantidadeCortesias;
}
