package br.com.jbst.controller;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.components.MatriculasMessageProducer;
import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.TurmasRepository;

@RestController
@RequestMapping("/teste-alerta")
public class TesteAlertaController {

    @Autowired
    private TurmasRepository turmasRepository;

    @Autowired
    private MatriculasMessageProducer producer;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
        .ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"))
        .withZone(ZoneId.of("America/Sao_Paulo"));

    @Transactional(readOnly = true)
    @GetMapping("/vencimento-hoje")
    public ResponseEntity<String> testarEnvio() {
        UUID id = UUID.fromString("e28cb94b-b464-4368-9ecb-e74fd1d2a68b");
        Optional<Turmas> turmaOpt = turmasRepository.findTurmaComEmpresa(id);

        if (turmaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Turma não encontrada.");
        }

        Turmas turma = turmaOpt.get();

        // Coletar todas empresas únicas
        List<Empresa> empresas = turma.getMatricula().stream()
            .filter(m -> m.getFuncionario() != null && m.getFuncionario().getEmpresa() != null)
            .map(m -> m.getFuncionario().getEmpresa())
            .distinct()
            .collect(Collectors.toList());

        if (empresas.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma empresa vinculada à turma.");
        }

        empresas.forEach(empresa -> {
            if (empresa.getEmail_usuario() != null && !empresa.getEmail_usuario().isBlank()) {
                MailSenderDto dto = criarEmailDeAlerta(turma, empresa);
                producer.sendMessage(dto);
            }
        });

        return ResponseEntity.ok("✅ Alertas enviados para " + empresas.size() + " empresas!");
    }

    private MailSenderDto criarEmailDeAlerta(Turmas turma, Empresa empresa) {
        // Formatar datas
        String dataValidade = turma.getValidadedocurso() != null ? 
            DATE_FORMATTER.format(turma.getValidadedocurso()) : "Data não informada";
        
        String dataInicio = turma.getDatainicio() != null ? 
            DATE_FORMATTER.format(turma.getDatainicio()) : "Data não informada";
        
        String dataFim = turma.getDatafim() != null ? 
            DATE_FORMATTER.format(turma.getDatafim()) : "Data não informada";

        // Listar funcionários da empresa
        String funcionarios = turma.getMatricula().stream()
            .filter(m -> m.getFuncionario() != null 
                && m.getFuncionario().getEmpresa() != null 
                && m.getFuncionario().getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa()))
            .map(m -> {
                String nome = m.getFuncionario().getNome() != null ? 
                    m.getFuncionario().getNome() : "Nome não informado";
                
                String cpf = m.getFuncionario().getCpf() != null ? 
                    formatarCPF(m.getFuncionario().getCpf()) : "CPF não informado";
                
                return String.format("<li>🔸 <strong>%s</strong> - CPF: %s</li>", nome, cpf);
            })
            .collect(Collectors.joining());

        if (funcionarios.isEmpty()) {
            funcionarios = "<li>⚠️ Nenhum funcionário encontrado</li>";
        }

        String corpoEmail = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto;">
                <div style="border: 2px solid #ff4444; border-radius: 10px; padding: 20px;">
                    <h2 style="color: #ff4444; margin-top: 0;">🚨 ATENÇÃO! VALIDADE EXPIRANDO</h2>
                    
                    <div style="margin-bottom: 15px;">
                        <p>📚 <strong>Curso:</strong> %s</p>
                        <p>🔢 <strong>Turma:</strong> %s</p>
                        <p>📅 <strong>Validade:</strong> %s</p>
                        <p>🗓️ <strong>Período:</strong> %s a %s</p>
                    </div>
                    
                    <h3 style="color: #555;">👥 Funcionários afetados:</h3>
                    <ul style="list-style: none; padding-left: 20px;">
                        %s
                    </ul>
                    
                    <div style="background-color: #fff3cd; padding: 15px; border-radius: 5px; margin-top: 20px;">
                        <p style="margin: 0;">⏳ <strong>Ação necessária:</strong> Renove o curso imediatamente para manter a validade dos certificados.</p>
                    </div>
                    
                    <hr style="border-color: #ddd; margin: 25px 0;">
                    
                    <div style="text-align: center; color: #666;">
                        <p>📞 Contato: (11) 1234-5678</p>
                        <p>✉️ Email: contato@jbseguranca.com.br</p>
                    </div>
                </div>
            </div>
            """,
            turma.getCurso() != null ? turma.getCurso().getCurso() : "Curso não informado",
            turma.getNumeroTurma() != null ? turma.getNumeroTurma().toString() : "Número não informado",
            dataValidade,
            dataInicio,
            dataFim,
            funcionarios
        );

        MailSenderDto dto = new MailSenderDto();
        dto.setMailTo(empresa.getEmail_usuario());
        dto.setSubject("⚠️ ALERTA: Validade do curso " + turma.getCurso().getCurso() + " expira hoje!");
        dto.setBody(corpoEmail);
        
        return dto;
    }

    private String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}