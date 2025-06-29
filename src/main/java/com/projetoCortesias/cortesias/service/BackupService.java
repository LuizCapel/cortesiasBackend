
package com.projetoCortesias.cortesias.service;

import com.projetoCortesias.cortesias.model.Cortesia;
import com.projetoCortesias.cortesias.model.Evento;
import com.projetoCortesias.cortesias.model.Pessoa;
import com.projetoCortesias.cortesias.model.Usuario;
import com.projetoCortesias.cortesias.repository.CortesiaRepository;
import com.projetoCortesias.cortesias.repository.EventoRepository;
import com.projetoCortesias.cortesias.repository.PessoaRepository;
import com.projetoCortesias.cortesias.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@EnableScheduling
public class BackupService {

    @Autowired
    private EventoRepository eventoRepo;
    @Autowired
    private PessoaRepository pessoaRepo;
    @Autowired
    private CortesiaRepository cortesiaRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(cron = "0 0 5 * * *") // diariamente às 2h da manhã
    public void gerarEEnviarBackup() throws IOException, MessagingException {
        File backup = gerarArquivoBackup();
        enviarEmailComBackup(backup);
        backup.delete(); // opcional: remover arquivo após envio
    }

    private File gerarArquivoBackup() throws IOException {
        File file = new File("backup.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("===== EVENTOS =====\n");
            List<Evento> eventos = eventoRepo.findAll();
            for (Evento e : eventos) {
                writer.write(String.format("%d,%s,%s,%s,%s,%d\n",
                        e.getId(), e.getNome(), e.getDataInicio(), e.getDataFim(),
                        e.getLocal(), e.getQuantidadeCortesias()));
            }

            writer.write("\n===== PESSOAS =====\n");
            List<Pessoa> pessoas = pessoaRepo.findAll();
            for (Pessoa p : pessoas) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        p.getId(), p.getNome(), p.getDataNascimento(), p.getCpf(),
                        p.getCidade(), p.getTelefone(), p.getEmail()));
            }

            writer.write("\n===== USUÁRIOS =====\n");
            List<Usuario> usuarios = usuarioRepo.findAll();
            for (Usuario u : usuarios) {
                writer.write(String.format("%d,%s,%s,%s\n",
                        u.getId(), u.getNome(), u.getEmail(), u.getPermissoes().toString()));
            }

            writer.write("\n===== CORTESIAS =====\n");
            List<Cortesia> cortesias = cortesiaRepo.findAll();
            for (Cortesia c : cortesias) {
                writer.write(String.format("%d,%d,%d,%s,%b\n",
                        c.getId(),
                        c.getEvento() != null ? c.getEvento().getId() : null,
                        c.getPessoa() != null ? c.getPessoa().getId() : null,
                        c.getDataSolicitacao(),
                        c.isResgatada()));
            }

            writer.write("\n===== INSERTS =====\n");

            for (Evento e : eventos) {
                writer.write(String.format(
                        "INSERT INTO evento (id, nome, data_inicio, data_fim, local, quantidade_cortesias) VALUES (%d, '%s', '%s', '%s', '%s', %d);\n",
                        e.getId(), e.getNome(), e.getDataInicio(), e.getDataFim(), e.getLocal(), e.getQuantidadeCortesias()));
            }

            for (Pessoa p : pessoas) {
                writer.write(String.format(
                        "INSERT INTO pessoa (id, nome, data_nascimento, cpf, cidade, telefone, email) VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');\n",
                        p.getId(), p.getNome(), p.getDataNascimento(), p.getCpf(), p.getCidade(), p.getTelefone(), p.getEmail()));
            }

            for (Usuario u : usuarios) {
                writer.write(String.format(
                        "INSERT INTO usuario (id, nome, email, senha) VALUES (%d, '%s', '%s', '[SENHA_OMITIDA]');\n",
                        u.getId(), u.getNome(), u.getEmail()));
                for (var perm : u.getPermissoes()) {
                    writer.write(String.format(
                            "INSERT INTO usuario_permissoes (usuario_id, permissoes) VALUES (%d, '%s');\n",
                            u.getId(), perm.name()));
                }
            }

            for (Cortesia c : cortesias) {
                writer.write(String.format(
                        "INSERT INTO cortesia (id, evento_id, pessoa_id, data_solicitacao, resgatada) VALUES (%d, %d, %d, '%s', %b);\n",
                        c.getId(),
                        c.getEvento() != null ? c.getEvento().getId() : null,
                        c.getPessoa() != null ? c.getPessoa().getId() : null,
                        c.getDataSolicitacao(),
                        c.isResgatada()));
            }
        }
        return file;
    }

    private void enviarEmailComBackup(File file) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("lascapel@uem.br");
        helper.setSubject("Backup Diário do Sistema de Cortesias");
        helper.setText("Segue em anexo o backup diário do banco de dados com dados e INSERTs.");

        FileSystemResource fileResource = new FileSystemResource(file);
        helper.addAttachment("backup.txt", fileResource);

        mailSender.send(message);
    }
}