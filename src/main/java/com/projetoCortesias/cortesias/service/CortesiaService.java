package com.projetoCortesias.cortesias.service;

import com.projetoCortesias.cortesias.model.Cortesia;
import com.projetoCortesias.cortesias.model.Evento;
import com.projetoCortesias.cortesias.model.Pessoa;
import com.projetoCortesias.cortesias.repository.CortesiaRepository;
import com.projetoCortesias.cortesias.repository.EventoRepository;
import com.projetoCortesias.cortesias.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CortesiaService {

    private final PessoaRepository pessoaRepository;
    private final EventoRepository eventoRepository;
    private final CortesiaRepository cortesiaRepository;

    public String solicitarCortesia(Long eventoId, String cpf) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Verifica se a pessoa já solicitou
        if (cortesiaRepository.findByEventoIdAndPessoaCpf(eventoId, cpf).isPresent()) {
            throw new RuntimeException("Pessoa já solicitou cortesia para este evento");
        }

        // Verifica se há cortesias disponíveis
        long emitidas = cortesiaRepository.countByEventoId(eventoId);
        if (emitidas >= evento.getQuantidadeCortesias()) {
            throw new RuntimeException("Cortesias esgotadas para este evento");
        }

        // Busca a pessoa cadastrada
        Pessoa pessoa = pessoaRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Pessoa ainda não cadastrada"));

        // Gera e salva a cortesia
        String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Cortesia cortesia = new Cortesia(null, evento, pessoa, codigo, false, LocalDate.now());
        cortesiaRepository.save(cortesia);

        return codigo;
    }

    public boolean validarCortesia(String codigo, Long eventoId) {
        Cortesia cortesia = cortesiaRepository.findByCodigoValidacao(codigo)
            .orElseThrow(() -> new RuntimeException("Código inválido"));

        if (!cortesia.getEvento().getId().equals(eventoId)) {
            throw new RuntimeException("Cortesia não pertence a este evento");
        }

        if (Boolean.TRUE.equals(cortesia.getResgatada())) {
            throw new RuntimeException("Cortesia já resgatada");
        }

        cortesia.setResgatada(true);
        cortesiaRepository.save(cortesia);

        return true;
    }
}
