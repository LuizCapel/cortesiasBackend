package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.dto.CortesiaDTO;
import com.projetoCortesias.cortesias.dto.CortesiaSolicitacaoDTO;
import com.projetoCortesias.cortesias.model.Cortesia;
import com.projetoCortesias.cortesias.model.Pessoa;
import com.projetoCortesias.cortesias.repository.CortesiaRepository;
import com.projetoCortesias.cortesias.service.CortesiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cortesias")
@RequiredArgsConstructor
public class CortesiaController {

    private final CortesiaService cortesiaService;
    private final CortesiaRepository cortesiaRepository;

//    @PostMapping("/evento/{eventoId}/solicitar")
//    public ResponseEntity<String> solicitar(@PathVariable Long eventoId, @RequestParam String cpf) {
//        try {
//            String codigo = cortesiaService.solicitarCortesia(eventoId, cpf);
//            return ResponseEntity.ok("Cortesia concedida. Código: " + codigo);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitar(@RequestBody CortesiaSolicitacaoDTO dto) {
        try {
            String codigo = cortesiaService.solicitarCortesia(dto.getEventoId(), dto.getCpf());
            return ResponseEntity.ok("Cortesia concedida. Código: " + codigo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/evento/{eventoId}/validar")
    public ResponseEntity<String> validar(@PathVariable Long eventoId, @RequestParam String codigo) {
        try {
            cortesiaService.validarCortesia(codigo, eventoId);
            return ResponseEntity.ok("Cortesia válida e resgatada.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<CortesiaDTO>> listarCortesiasPorEvento(
            @PathVariable Long eventoId,
            @RequestParam(required = false) Boolean resgatada) {

        List<Cortesia> cortesias;

        if (resgatada == null) {
            cortesias = cortesiaRepository.findByEventoId(eventoId);
        } else {
            cortesias = cortesiaRepository.findByEventoIdAndResgatada(eventoId, resgatada);
        }

        List<CortesiaDTO> dtos = cortesias.stream().map(c -> {
            CortesiaDTO dto = new CortesiaDTO();
            dto.setCodigo(c.getCodigoValidacao());
            dto.setResgatada(c.isResgatada());
            dto.setPessoaNome(c.getPessoa().getNome());
            dto.setPessoaCpf(c.getPessoa().getCpf());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/resgatar")
    public ResponseEntity<String> marcarComoResgatada(@RequestParam String codigo) {
        Optional<Cortesia> opt = cortesiaRepository.findByCodigoValidacao(codigo);
        if (opt.isEmpty()) return ResponseEntity.badRequest().body("Cortesia não encontrada");

        Cortesia cortesia = opt.get();
        if (cortesia.isResgatada()) return ResponseEntity.badRequest().body("Cortesia já foi resgatada");

        cortesia.setResgatada(true);
        cortesiaRepository.save(cortesia);

        return ResponseEntity.ok("Cortesia marcada como resgatada.");
    }
}
