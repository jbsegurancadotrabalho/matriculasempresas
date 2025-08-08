package br.com.jbst.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DtoEAD.GetEvidenciaEmpresaDTO;
import br.com.jbst.DtoEAD.PostEvidenciaEmpresaDTO;
import br.com.jbst.components.MatriculasMessageProducer;
import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Evidencias;
import br.com.jbst.entities.Instrutor;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Proficiencia;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.EvidenciasRepository;
import br.com.jbst.repositories.InstrutorRepository;
import br.com.jbst.repositories.MatriculasRepository;
import br.com.jbst.repositories.TurmasRepository;

@Service
public class HistoricoEvidenciasService {

    @Autowired private EvidenciasRepository evidenciasRepository;
    @Autowired private MatriculasRepository matriculasRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private TurmasRepository turmasRepository;
    @Autowired private InstrutorRepository instrutorRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private MatriculasMessageProducer matriculasMessageProducer;

    public GetEvidenciaEmpresaDTO criarEvidencia(PostEvidenciaEmpresaDTO dto, byte[] evidenciaBytes) {
        Evidencias evidencia = new Evidencias();
        evidencia.setIdEvidencias(UUID.randomUUID());
        evidencia.setDataHoraCriacao(Instant.now());
        evidencia.setNome(dto.getNome());
        evidencia.setDescricao(dto.getDescricao());
        evidencia.setInserir_evidencias(evidenciaBytes);

        Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        evidencia.setEmpresa(empresa);

        Turmas turma = turmasRepository.findById(dto.getIdTurmas())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        evidencia.setTurmas(turma);

        Evidencias evidenciaSalva = evidenciasRepository.save(evidencia);
        enviarEmailRelatorioEvidenciaComProficiencia(empresa, turma, evidenciaSalva);

        return modelMapper.map(evidenciaSalva, GetEvidenciaEmpresaDTO.class);
    }

    private void enviarEmailRelatorioEvidenciaComProficiencia(Empresa empresa, Turmas turma, Evidencias evidencia) {
        if (empresa.getEmail_usuario() == null || empresa.getEmail_usuario().isBlank()) return;

        List<Matriculas> matriculas = matriculasRepository
                .findByTurmas_IdTurmasAndFuncionario_Empresa_IdEmpresa(turma.getIdTurmas(), empresa.getIdEmpresa());

        List<Instrutor> instrutores = instrutorRepository.findInstrutorComProficiencia(turma.getIdTurmas());

        String htmlInstrutores = gerarHtmlInstrutoresComProficiencias(instrutores);
        String htmlCertificados = gerarHtmlCertificados(matriculas);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        String dataInicio = turma.getDatainicio() != null ? formatter.format(turma.getDatainicio()) : "Não informada";
        String dataFim = turma.getDatafim() != null ? formatter.format(turma.getDatafim()) : "Não informada";
        String cursoNome = turma.getCurso() != null && turma.getCurso().getCurso() != null ? turma.getCurso().getCurso() : "Não informado";
        String numeroTurma = turma.getNumeroTurma() != null ? turma.getNumeroTurma().toString() : "Não informado";
        String razaoSocial = empresa.getRazaosocial() != null ? empresa.getRazaosocial() : "Não informado";
        String nomeEvidencia = evidencia.getNome() != null ? evidencia.getNome() : "Não informado";
        String descricaoEvidencia = evidencia.getDescricao() != null ? evidencia.getDescricao() : "Sem descrição.";

        String corpoEmail = String.format("""
            <h2 style='color: #28a745;'>Relatório de Evidência</h2>
            <p><strong>Empresa:</strong> %s</p>
            <p><strong>Curso:</strong> %s</p>
            <p><strong>Turma:</strong> %s</p>
            <p><strong>Período:</strong> %s a %s</p>
            <p><strong>Nome da Evidência:</strong> %s</p>
            <p><strong>Descrição:</strong> %s</p>
            %s
            <h3 style='color: #28a745;'>Certificados dos colaboradores:</h3>
            <ul>%s</ul>
        """, razaoSocial, cursoNome, numeroTurma, dataInicio, dataFim, nomeEvidencia, descricaoEvidencia, htmlInstrutores, htmlCertificados);

        MailSenderDto dtoEvidencia = new MailSenderDto();
        dtoEvidencia.setMailTo(empresa.getEmail_usuario());
        dtoEvidencia.setSubject(String.format("Lista de Presença - %s | Curso: %s | Turma: %s | %s a %s",
                nomeEvidencia, cursoNome, numeroTurma, dataInicio, dataFim));
        dtoEvidencia.setBody(corpoEmail);

        if (evidencia.getInserir_evidencias() != null && evidencia.getInserir_evidencias().length > 0) {
            dtoEvidencia.setAttachment(evidencia.getInserir_evidencias());
            dtoEvidencia.setAttachmentName("Lista_de_Presenca_" + evidencia.getIdEvidencias() + ".pdf");
        }

        try {
            matriculasMessageProducer.sendMessage(dtoEvidencia);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Instrutor instrutor : instrutores) {
            if (instrutor.getProficiencia() != null && instrutor.getProficiencia().length > 0) {
                MailSenderDto dto = new MailSenderDto();
                dto.setMailTo(empresa.getEmail_usuario());
                dto.setSubject("Proficiencia Geral - " + instrutor.getInstrutor());
                dto.setBody("<p>Proficiência geral do instrutor " + instrutor.getInstrutor() + ".</p>");
                dto.setAttachment(instrutor.getProficiencia());
                dto.setAttachmentName("ProficienciaGeral_" + instrutor.getInstrutor().replaceAll("\\s+", "_") + ".pdf");

                try {
                    matriculasMessageProducer.sendMessage(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<Proficiencia> profs = instrutor.getProficiencias();
            if (profs != null) {
                for (Proficiencia p : profs) {
                    if (p.getInserir_proficiencia() != null && p.getInserir_proficiencia().length > 0) {
                        MailSenderDto dto = new MailSenderDto();
                        dto.setMailTo(empresa.getEmail_usuario());
                        dto.setSubject("Proficiencia - " + p.getProficiencia());
                        dto.setBody("<p>Proficiência: <strong>" + p.getProficiencia() + "</strong><br>Instrutor: " + instrutor.getInstrutor() + "</p>");
                        dto.setAttachment(p.getInserir_proficiencia());
                        dto.setAttachmentName("Proficiencia_" + p.getProficiencia().replaceAll("\\s+", "_") + ".pdf");

                        try {
                            matriculasMessageProducer.sendMessage(dto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        String linkCertificado = "http://jbseguranca.s3-website.us-east-2.amazonaws.com/certificado-do-funcionario-valido/" + evidencia.getIdEvidencias();
        gerarLinkWhatsApp(empresa, linkCertificado, cursoNome, numeroTurma);
    }

    private String gerarHtmlCertificados(List<Matriculas> matriculas) {
        if (matriculas == null || matriculas.isEmpty()) return "<p>Nenhuma matrícula encontrada.</p>";

        return matriculas.stream().map(m -> {
            String nome = m.getFuncionario() != null ? m.getFuncionario().getNome() : "Nome não encontrado";
            String cpf = m.getFuncionario() != null ? m.getFuncionario().getCpf() : "CPF não encontrado";
            String idMatricula = m.getIdMatricula().toString();
            return String.format("""
                <li><strong>%s</strong> - CPF: %s<br>
                <a href='http://jbseguranca.s3-website.us-east-2.amazonaws.com/certificado-do-funcionario-valido/%s' target='_blank'>
                📄 Baixar certificado (Matrícula %s)</a></li>
            """, nome, cpf, idMatricula, idMatricula);
        }).collect(Collectors.joining());
    }

    private String gerarHtmlInstrutoresComProficiencias(List<Instrutor> instrutores) {
        if (instrutores == null || instrutores.isEmpty()) return "<p><strong>Instrutores:</strong> Nenhum instrutor encontrado.</p>";

        return instrutores.stream().map(instrutor -> {
            String nome = instrutor.getInstrutor();
            String cpf = instrutor.getCpf();
            String profBin = (instrutor.getProficiencia() != null && instrutor.getProficiencia().length > 0) ? "Sim" : "Não";

            String profHtml = "";
            List<Proficiencia> profs = instrutor.getProficiencias();
            if (profs != null && !profs.isEmpty()) {
                profHtml = profs.stream().map(p -> String.format("<li><strong>%s</strong> - %s</li>",
                        p.getProficiencia(), p.getDescricao() != null ? p.getDescricao() : "Sem descrição"))
                        .collect(Collectors.joining());
                profHtml = "<ul>" + profHtml + "</ul>";
            } else {
                profHtml = "<p>Sem proficiências cadastradas.</p>";
            }

            return String.format("""
                <p><strong>Instrutor:</strong> %s</p>
                <p><strong>CPF:</strong> %s</p>
                <p><strong>Proficiência binária registrada?</strong> %s</p>
                <p><strong>Proficiencias declaradas:</strong></p>
                %s
                <hr>
            """, nome, cpf, profBin, profHtml);
        }).collect(Collectors.joining());
    }

    private void gerarLinkWhatsApp(Empresa empresa, String linkCertificado, String curso, String turma) {
        String telefone = empresa.getTelefone_responsavel();
        if (telefone == null || telefone.isBlank()) return;

        String numeroFormatado = telefone.replaceAll("\\D+", "");
        if (!numeroFormatado.startsWith("55")) numeroFormatado = "55" + numeroFormatado;

        String mensagem = String.format("""
            Olá, %s! 👋

            Segue o relatório de evidência do curso *%s*, turma *%s*.

            📄 Acesse o certificado:
            %s
        """, empresa.getRazaosocial(), curso, turma, linkCertificado);

        try {
            String mensagemEncoded = URLEncoder.encode(mensagem, StandardCharsets.UTF_8);
            String url = "https://wa.me/" + numeroFormatado + "?text=" + mensagemEncoded;
            System.out.println("🔗 Link para WhatsApp:");
            System.out.println(url);
        } catch (Exception e) {
            System.err.println("Erro ao gerar link do WhatsApp:");
            e.printStackTrace();
        }
    }
}