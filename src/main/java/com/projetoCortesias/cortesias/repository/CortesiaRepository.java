package com.projetoCortesias.cortesias.repository;

import com.projetoCortesias.cortesias.model.Cortesia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CortesiaRepository extends JpaRepository<Cortesia, Long> {
    Optional<Cortesia> findByEventoIdAndPessoaCpf(Long eventoId, String cpf);
    Optional<Cortesia> findByCodigoValidacao(String codigo);
    Long countByEventoId(Long eventoId);

    List<Cortesia> findByEventoId(Long eventoId);
    List<Cortesia> findByEventoIdAndResgatada(Long eventoId, Boolean resgatada);
}
