package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.model.Carro;
import com.projetoCortesias.cortesias.repository.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carros")
@RequiredArgsConstructor
public class CarroController {

    private final CarroRepository carroRepository;

    @GetMapping
    public ResponseEntity<List<Carro>> listarCarros() {
        return ResponseEntity.ok(carroRepository.findAll());
    }

}