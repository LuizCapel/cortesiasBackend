package com.projetoCortesias.cortesias.repository;

import com.projetoCortesias.cortesias.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByCpf(String cpf);

    @Query("SELECT p FROM Pessoa p WHERE " +
            "(:nome IS NULL OR p.nome ILIKE %:nome%) AND " +
            "(:cidade IS NULL OR p.cidade ILIKE %:cidade%) AND " +
            "(:cpf IS NULL OR p.cpf LIKE %:cpf%)")
    List<Pessoa> buscarComFiltros(@Param("nome") String nome,
                                  @Param("cidade") String cidade,
                                  @Param("cpf") String cpf);
}
