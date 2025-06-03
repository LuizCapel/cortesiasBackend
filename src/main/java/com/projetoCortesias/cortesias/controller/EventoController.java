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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final EventoRepository eventoRepository;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Evento evento) {
        if (evento.getQuantidadeCortesias() < 0) {
            return ResponseEntity.badRequest().body("Quantidade de cortesias não pode ser negativa");
        }

        if (eventoRepository.existsByResponsavelAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(evento.getResponsavel(), evento.getDataFim(), evento.getDataInicio())) {
            return ResponseEntity.badRequest().body("Já existe um evento para este responsável nesse período.");
        }

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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data,
            @RequestParam(required = false) String local,
            @RequestParam(required = false) String responsavel) {

        List<Evento> eventos = eventoRepository.findAll();

        List<Evento> filtrados = eventos.stream()
                .filter(e -> nome == null || e.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(e -> local == null || e.getLocal().toLowerCase().contains(local.toLowerCase()))
                .filter(e -> responsavel == null || e.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()))
                .filter(e -> data == null ||
                        (e.getDataInicio() != null && e.getDataFim() != null &&
                                !e.getDataInicio().after(data) && !e.getDataFim().before(data)))
                .toList();

        return ResponseEntity.ok(filtrados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody EventoDTO dto) {
        Evento e = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (eventoRepository.existeEventoSobreposto(id, dto.getResponsavel(), dto.getDataInicio(), dto.getDataFim())) {
            return ResponseEntity.badRequest().body("Já existe um evento para este responsável nesse período.");
        }

        e.setNome(dto.getNome());
        e.setDataInicio(dto.getDataInicio());
        e.setDataFim(dto.getDataFim());
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
