
package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.dto.PessoaDTO;
import com.projetoCortesias.cortesias.model.Cortesia;
import com.projetoCortesias.cortesias.model.Evento;
import com.projetoCortesias.cortesias.model.Pessoa;
import com.projetoCortesias.cortesias.repository.CortesiaRepository;
import com.projetoCortesias.cortesias.repository.EventoRepository;
import com.projetoCortesias.cortesias.repository.PessoaRepository;
import com.projetoCortesias.cortesias.service.CortesiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api/cortesias/publico")
@RequiredArgsConstructor
public class CortesiaPublicaController {

    @Autowired
    private EventoRepository eventoRepo;

    @Autowired
    private PessoaRepository pessoaRepo;

    @Autowired
    private CortesiaRepository cortesiaRepo;

    @Autowired
    private final CortesiaService cortesiaService;

    @GetMapping("/disponiveis/{eventoId}")
    public ResponseEntity<?> verificarDisponibilidade(@PathVariable Long eventoId) {
        Evento evento = eventoRepo.findById(eventoId).orElse(null);
        if (evento == null) return ResponseEntity.notFound().build();

        long emitidas = cortesiaRepo.countByEventoId(eventoId);
        boolean disponivel = emitidas < evento.getQuantidadeCortesias();

        return ResponseEntity.ok().body(java.util.Map.of("disponivel", disponivel));
    }

    @PostMapping("/solicitar/{eventoId}")
    public ResponseEntity<?> solicitarCortesia(@PathVariable Long eventoId, @RequestBody PessoaDTO pessoaDto) {
        Evento evento = eventoRepo.findById(eventoId).orElse(null);
        if (evento == null) return ResponseEntity.badRequest().body("Evento inválido.");

        long emitidas = cortesiaRepo.countByEventoId(eventoId);
        if (emitidas >= evento.getQuantidadeCortesias())
            return ResponseEntity.badRequest().body("Cortesias esgotadas.");

        Pessoa pessoa = pessoaRepo.findByCpf(pessoaDto.getCpf())
                .orElseGet(() -> {
                    Pessoa nova = new Pessoa();
                    nova.setCpf(pessoaDto.getCpf());
                    nova.setNome(pessoaDto.getNome());
                    nova.setCidade(pessoaDto.getCidade());
                    nova.setTelefone(pessoaDto.getTelefone());
                    nova.setEmail(pessoaDto.getEmail());
                    nova.setDataNascimento(pessoaDto.getDataNascimento());
                    return pessoaRepo.save(nova);
                });

        if (cortesiaRepo.existsByEventoIdAndPessoaId(eventoId, pessoa.getId()))
            return ResponseEntity.badRequest().body("Você já solicitou uma cortesia para este evento.");

        String codigo = cortesiaService.solicitarCortesia(eventoId, pessoa.getCpf());
        return ResponseEntity.ok("Cortesia concedida. Código: " + codigo);
    }
}
