package br.com.jbst.components;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.TurmasRepository;

@Component
public class AlertaVencimentoTurmasScheduler {

    @Autowired
    private TurmasRepository turmasRepository;

    @Autowired
    private MatriculasMessageProducer producer;

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("pt", "BR")).withZone(ZoneId.of("America/Sao_Paulo"));

    @Scheduled(cron = "0 23 11 * * *") // Executa todos os dias às 08:45
    public void enviarAlertasDeVencimentoHoje() {
        Instant hojeInicio = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant hojeFim = hojeInicio.plus(1, ChronoUnit.DAYS);
        List<Turmas> turmas = turmasRepository.findTurmasVencendoHojeComEmpresas(hojeInicio, hojeFim);

        for (Turmas turma : turmas) {
            Set<Empresa> empresasUnicas = obterEmpresasDaTurma(turma);
            for (Empresa empresa : empresasUnicas) {
                if (empresa.getEmail_usuario() != null && !empresa.getEmail_usuario().isBlank()) {
                    MailSenderDto dto = criarEmailDeAlerta(turma, empresa);
                    producer.sendMessage(dto);
                }
            }
        }
    }

    private Set<Empresa> obterEmpresasDaTurma(Turmas turma) {
        Set<Empresa> empresas = new HashSet<>();
        turma.getMatricula().forEach(matricula -> {
            if (matricula.getFuncionario() != null && matricula.getFuncionario().getEmpresa() != null) {
                empresas.add(matricula.getFuncionario().getEmpresa());
            }
        });
        return empresas;
    }

    private MailSenderDto criarEmailDeAlerta(Turmas turma, Empresa empresa) {
        String validadeCursoFormatada = FORMATTER.format(turma.getValidadedocurso());
        String dataInicioFormatada = FORMATTER.format(turma.getDatainicio());
        String dataFimFormatada = FORMATTER.format(turma.getDatafim());
        String nomeCurso = turma.getCurso() != null && turma.getCurso().getCurso() != null
                ? turma.getCurso().getCurso()
                : "Curso não informado";

        List<Matriculas> matriculasEmpresa = turma.getMatricula().stream()
                .filter(m -> m.getFuncionario() != null &&
                        m.getFuncionario().getEmpresa() != null &&
                        m.getFuncionario().getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa()))
                .collect(Collectors.toList());

        String listaFuncionarios = matriculasEmpresa.stream().map(m -> {
            String nome = m.getFuncionario() != null ? m.getFuncionario().getNome() : "Nome não encontrado";
            String cpf = m.getFuncionario() != null ? m.getFuncionario().getCpf() : "CPF não encontrado";
            return String.format("<li>👤 <strong>%s</strong> - CPF: %s</li>", nome, cpf);
        }).collect(Collectors.joining());

        if (listaFuncionarios.isBlank()) {
            listaFuncionarios = "<li>Nenhum funcionário vinculado a esta empresa nesta turma.</li>";
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
                        <p>📞 Contato: (21) 3933-1161 e (11) 2694-2399 - Telefone e WhatSapp</p>
                        <p>✉️ Email: operacional@jbsegurancadotrabalho.com.br</p>
                    </div>
                </div>
            </div>
        """, nomeCurso, turma.getNumeroTurma(), validadeCursoFormatada, dataInicioFormatada, dataFimFormatada, listaFuncionarios);

        MailSenderDto dto = new MailSenderDto();
        dto.setMailTo(empresa.getEmail_usuario());
        dto.setSubject("⚠️ Certificados vencem hoje - Turma " + turma.getNumeroTurma());
        dto.setBody(corpoEmail);
        return dto;
    }
}
