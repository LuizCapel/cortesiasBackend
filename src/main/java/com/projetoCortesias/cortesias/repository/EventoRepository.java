package com.projetoCortesias.cortesias.repository;

import com.projetoCortesias.cortesias.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

//    @Query("SELECT e FROM Evento e WHERE " +
//            "(:nome IS NULL OR e.nome ILIKE %:nome%) AND " +
//            "(:data IS NULL OR (e.dataInicio <= :data AND e.dataFim >= :data)) AND " +
//            "(:local IS NULL OR e.local ILIKE %:local%) AND " +
//            "(:responsavel IS NULL OR e.responsavel ILIKE %:responsavel%)")
//    List<Evento> buscarComFiltros(@Param("nome") String nome,
//                                  @Param("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
//                                  @Param("local") String local,
//                                  @Param("responsavel") String responsavel);
    List<Evento> findAll();

//    boolean existsByDataAndResponsavel(Date data, String responsavel);
    boolean existsByResponsavelAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
            String responsavel, Date novaDataFim, Date novaDataInicio);

    @Query("SELECT COUNT(e) > 0 FROM Evento e WHERE " +
            "e.responsavel = :responsavel AND " +
            "e.id <> :id AND " +
            "e.dataInicio <= :novaDataFim AND " +
            "e.dataFim >= :novaDataInicio")
    boolean existeEventoSobreposto(@Param("id") Long id,
                                   @Param("responsavel") String responsavel,
                                   @Param("novaDataInicio") Date novaDataInicio,
                                   @Param("novaDataFim") Date novaDataFim);
}
