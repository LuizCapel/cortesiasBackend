package com.projetoCortesias.cortesias.repository;

import com.projetoCortesias.cortesias.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e WHERE " +
            "(:nome IS NULL OR e.nome ILIKE %:nome%) AND " +
            "(:data IS NULL OR e.data = :data) AND " +
            "(:local IS NULL OR e.local ILIKE %:local%) AND " +
            "(:responsavel IS NULL OR e.responsavel ILIKE %:responsavel%)")
    List<Evento> buscarComFiltros(@Param("nome") String nome,
                                  @Param("data") LocalDate data,
                                  @Param("local") String local,
                                  @Param("responsavel") String responsavel);

    boolean existsByDataAndResponsavel(LocalDate data, String responsavel);
}
