package com.projetoCortesias.cortesias.service;

import com.projetoCortesias.cortesias.model.Evento;
import com.projetoCortesias.cortesias.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public Evento cadastrarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }
}
