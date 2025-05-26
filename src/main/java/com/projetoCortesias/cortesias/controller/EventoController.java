package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.dto.EventoDTO;
import com.projetoCortesias.cortesias.model.Evento;
import com.projetoCortesias.cortesias.repository.EventoRepository;
import com.projetoCortesias.cortesias.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;

    @PostMapping
    public ResponseEntity<Evento> criarEvento(@RequestBody EventoDTO dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setData(dto.getData());
        evento.setLocal(dto.getLocal());
        evento.setResponsavel(dto.getResponsavel());
        evento.setQuantidadeCortesias(dto.getQuantidadeCortesias());

        Evento salvo = eventoRepository.save(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Evento>> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) String local,
            @RequestParam(required = false) String responsavel) {
        return ResponseEntity.ok(eventoRepository.buscarComFiltros(nome, data, local, responsavel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizar(@PathVariable Long id, @RequestBody EventoDTO dto) {
        Evento e = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        e.setNome(dto.getNome());
        e.setData(dto.getData());
        e.setLocal(dto.getLocal());
        e.setResponsavel(dto.getResponsavel());
        e.setQuantidadeCortesias(dto.getQuantidadeCortesias());

        return ResponseEntity.ok(eventoRepository.save(e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!eventoRepository.existsById(id)) return ResponseEntity.notFound().build();
        eventoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
