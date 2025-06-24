package com.projetoCortesias.cortesias.repository;

import com.projetoCortesias.cortesias.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    List<Carro> findAll();

}
