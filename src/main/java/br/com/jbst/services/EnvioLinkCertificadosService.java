package br.com.jbst.services;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.MatriculasRepository;
import br.com.jbst.components.MatriculasMessageProducer;

@Service
public class EnvioLinkCertificadosService {

    @Autowired
    private MatriculasRepository matriculasRepository;

    @Autowired
    private MatriculasMessageProducer messageProducer;

    public void enviarLinksPorEmail(Empresa empresa, Turmas turma) {
        // Verifica se o e-mail está presente
        if (empresa.getEmail_usuario() == null || empresa.getEmail_usuario().isBlank()) return;

        // Busca matrículas da empresa vinculadas à turma
        List<Matriculas> matriculas = matriculasRepository
            .findByTurmas_IdTurmasAndFuncionario_Empresa_IdEmpresa(turma.getIdTurmas(), empresa.getIdEmpresa());

        // Geração de HTML dos certificados
        String htmlCertificados = gerarHtmlCertificados(matriculas);

        // Formata datas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        String dataInicio = formatter.format(turma.getDatainicio());
        String dataFim = formatter.format(turma.getDatafim());
        String curso = turma.getCurso().getCurso();
        String numeroTurma = String.valueOf(turma.getNumeroTurma());
        String responsavel = empresa.getResponsavel_sistema() != null ? empresa.getResponsavel_sistema() : "Prezado(a)";
     // Monta o corpo humanizado do e-mail
        String corpoEmail = String.format("""
            <div style="font-family: Arial, sans-serif; color: #333;">
                <p>Olá, <strong>%s</strong>!</p>

                <p>Conforme o encerramento da turma abaixo, seguem os <strong>links dos certificados</strong> dos colaboradores para seu acompanhamento e arquivamento:</p>

                <p>
                    📚 <strong>Curso:</strong> %s<br>
                    🔢 <strong>Turma:</strong> %s<br>
                    📅 <strong>Período:</strong> %s a %s
                </p>

                <h4 style="color: #28a745;">👥 Funcionários certificados:</h4>
                <ul>%s</ul>

                <p>Você também pode acessar esses certificados diretamente em nosso sistema, utilizando o e-mail de acesso da empresa: <strong>%s</strong>.</p>
                <p>Caso não lembre a senha, clique em <strong>"Esqueci minha senha"</strong> na página de login e você receberá uma nova por e-mail.<br>
                Se preferir, entre em contato com nossa equipe para maiores esclarecimentos.</p>

                <p style="margin-top: 30px;">
                    Atenciosamente,<br>
                    <strong>Equipe JB Segurança do Trabalho</strong><br>
                    📞 (11) 2694-2399 - (21) 3933-1161 - Telefone e WhatsApp<br>
                    ✉️ operacional@jbsegurancadotrabalho.com.br<br>
                    🌐 <a href="https://www.jbsegurancadotrabalho.com.br" target="_blank">https://www.jbsegurancadotrabalho.com.br</a>
                </p>
            </div>
        """, responsavel, curso, numeroTurma, dataInicio, dataFim, htmlCertificados, empresa.getEmail_usuario());

        // Prepara DTO para envio
        MailSenderDto mail = new MailSenderDto();
        mail.setMailTo(empresa.getEmail_usuario());
        mail.setSubject(String.format("Certificados - Curso: %s | Turma: %s", curso, numeroTurma));
        mail.setBody(corpoEmail);

        // Envia o e-mail via fila
        messageProducer.sendMessage(mail);
    }

    private String gerarHtmlCertificados(List<Matriculas> matriculas) {
        return matriculas.stream().map(m -> {
            String nome = m.getFuncionario().getNome();
            String cpf = m.getFuncionario().getCpf();
            String idMatricula = m.getIdMatricula().toString();
            return String.format("""
                <li><strong>%s</strong> - CPF: %s<br>
                📄 <a href='http://jbseguranca.s3-website.us-east-2.amazonaws.com/certificado-do-funcionario-valido/%s' target='_blank'>
                Baixar certificado</a></li>
            """, nome, cpf, idMatricula);
        }).collect(Collectors.joining());
    }
}
