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

    List<Evento> findAll();

    @Query("SELECT e FROM Evento e WHERE e.dataInicio BETWEEN :inicio AND :fim or e.dataFim BETWEEN :inicio AND :fim or :inicio between e.dataInicio and e.dataFim")
    List<Evento> findByDataEventoBetween(@Param("inicio") Date inicio, @Param("fim") Date fim);

//    boolean existsByResponsavelAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
//            String responsavel, Date novaDataFim, Date novaDataInicio);

    boolean existsByNomeAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
            String nome, Date novaDataFim, Date novaDataInicio);

//    @Query("SELECT COUNT(e) > 0 FROM Evento e WHERE " +
//            "e.responsavel = :responsavel AND " +
//            "e.id <> :id AND " +
//            "e.dataInicio <= :novaDataFim AND " +
//            "e.dataFim >= :novaDataInicio")
//    boolean existeEventoSobreposto(@Param("id") Long id,
//                                   @Param("responsavel") String responsavel,
//                                   @Param("novaDataInicio") Date novaDataInicio,
//                                   @Param("novaDataFim") Date novaDataFim);

    @Query("SELECT COUNT(e) > 0 FROM Evento e WHERE " +
            "e.nome = :nome AND " +
            "e.id <> :id AND " +
            "e.dataInicio <= :novaDataFim AND " +
            "e.dataFim >= :novaDataInicio")
    boolean existeEventoSobreposto(@Param("id") Long id,
                                   @Param("nome") String nome,
                                   @Param("novaDataInicio") Date novaDataInicio,
                                   @Param("novaDataFim") Date novaDataFim);
}
