package com.projetoCortesias.cortesias.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cortesia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Evento evento;

    @ManyToOne
    private Pessoa pessoa;

    @Column(unique = true)
    private String codigoValidacao;

    private Boolean resgatada = false;

    private LocalDate dataSolicitacao;

    public boolean isResgatada() {
        return resgatada;
    }
}
