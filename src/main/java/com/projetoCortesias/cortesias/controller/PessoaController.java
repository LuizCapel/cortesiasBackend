package com.projetoCortesias.cortesias.controller;

import com.projetoCortesias.cortesias.dto.PessoaDTO;
import com.projetoCortesias.cortesias.model.Pessoa;
import com.projetoCortesias.cortesias.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    @PostMapping
    public ResponseEntity<?> cadastrarPessoa(@RequestBody PessoaDTO dto) {
        if (pessoaRepository.findByCpf(dto.getCpf()).isPresent()) {
            return ResponseEntity.badRequest().body("CPF já cadastrado para outra pessoa");
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setCpf(dto.getCpf());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setCidade(dto.getCidade());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());

        Pessoa salva = pessoaRepository.save(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarTodas() {
        return ResponseEntity.ok(pessoaRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Pessoa>> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String cpf) {
        return ResponseEntity.ok(pessoaRepository.buscarComFiltros(nome, cidade, cpf));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @RequestBody PessoaDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        pessoa.setNome(dto.getNome());
        pessoa.setCpf(dto.getCpf());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setCidade(dto.getCidade());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setEmail(dto.getEmail());

        return ResponseEntity.ok(pessoaRepository.save(pessoa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pessoaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
