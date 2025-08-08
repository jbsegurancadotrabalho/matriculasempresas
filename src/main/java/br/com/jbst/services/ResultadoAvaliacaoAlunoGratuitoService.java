package br.com.jbst.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DTO.ResultadoAvaliacaoGratuitaDto;
import br.com.jbst.DTO.SendMessageZapDTO;
import br.com.jbst.DtoEAD.PostRespostaResultadoDto;
import br.com.jbst.DtoEAD.PostRespostaResultadoDto.RespostaDto;
import br.com.jbst.components.MatriculasMessageProducer;
import br.com.jbst.components.ZApiSenderComponent;
import br.com.jbst.entities.*;
import br.com.jbst.repositories.MatriculaGratuitaRepository;
import br.com.jbst.repositoriesEAD.AvaliacaoCursoRepository;
import br.com.jbst.repositoriesEAD.PerguntaRepository;
import br.com.jbst.repositoriesEAD.ResultadoAvaliacaoAlunoGratuitoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ResultadoAvaliacaoAlunoGratuitoService {

    @Autowired private ResultadoAvaliacaoAlunoGratuitoRepository resultadoRepository;
    @Autowired private AvaliacaoCursoRepository avaliacaoCursoRepository;
    @Autowired private PerguntaRepository perguntaRepository;
    @Autowired private MatriculaGratuitaRepository matriculasGratuitasRepository;
    @Autowired private MatriculasMessageProducer messageProducer;
    @Autowired private ZApiSenderComponent zapSender;
    @Autowired private ModelMapper modelMapper;

    public ResultadoAvaliacaoGratuitaDto responderAvaliacaoGratuita(PostRespostaResultadoDto dto, UUID idMatriculaGratuita) {
        MatriculasGratuita matricula = buscarMatricula(idMatriculaGratuita);
        AvaliacaoCurso avaliacao = buscarAvaliacao(dto.getIdAvaliacaoCurso());
        List<Pergunta> perguntas = perguntaRepository.findByAvaliacao_IdAvaliacaoCurso(dto.getIdAvaliacaoCurso());

        ResultadoAvaliacaoAlunoGratuito resultado = new ResultadoAvaliacaoAlunoGratuito();
        resultado.setIdResultado(UUID.randomUUID());
        resultado.setMatriculasGratuita(matricula);
        resultado.setAvaliacao(avaliacao);
        resultado.setDataResposta(Instant.now());

        int acertos = 0;
        List<RespostaSelecionadaGratuita> respostasSelecionadas = new ArrayList<>();

        for (RespostaDto respostaDto : dto.getRespostas()) {
            Pergunta pergunta = encontrarPerguntaPorId(perguntas, respostaDto.getIdPergunta());
            RespostaEad respostaSelecionada = encontrarRespostaPorId(pergunta, respostaDto.getIdResposta());

            if (respostaSelecionada.isCorreta()) {
                acertos++;
            }

            respostasSelecionadas.add(criarRespostaSelecionada(resultado, avaliacao, pergunta, respostaSelecionada));
        }

        double nota = calcularNota(acertos, perguntas.size());
        boolean aprovado = nota >= 7.0;

        resultado.setNota(nota);
        resultado.setAprovado(aprovado);
        resultado.setRespostasSelecionadasGratuito(respostasSelecionadas);

        resultadoRepository.save(resultado);

        enviarFeedbackResultado(resultado);

        return criarDtoResposta(resultado);
    }

    private void enviarFeedbackResultado(ResultadoAvaliacaoAlunoGratuito resultado) {
        MatriculasGratuita matricula = resultado.getMatriculasGratuita();
        String nome = matricula.getFuncionario();
        String email = matricula.getEmail();
        String telefone = matricula.getWhatsapp();

        String linkAvaliacao = "http://jbseguranca.s3-website.us-east-2.amazonaws.com/imprimir-avaliacao/" + resultado.getIdResultado();
        String linkCertificado = "http://jbseguranca.s3-website.us-east-2.amazonaws.com/gerar-certificado-gratuito/" + matricula.getIdMatriculaGratuita();

        String status = resultado.isAprovado() ? "APROVADO" : "REPROVADO";

        if (email != null && !email.isBlank()) {
            MailSenderDto mail = new MailSenderDto();
            mail.setMailTo(email);
            mail.setSubject("Resultado da Avaliação do Curso Gratuito");
            mail.setBody(String.format("""
                <p>Olá <strong>%s</strong>,</p>
                <p>Você concluiu a avaliação do curso gratuito.</p>
                <p><strong>Nota:</strong> %.2f</p>
                <p><strong>Status:</strong> %s</p>
                <p><a href=\"%s\">Clique aqui para imprimir sua avaliação</a></p>
                %s
                <p>Atenciosamente,<br>Equipe JB Segurança do Trabalho</p>
            """, nome, resultado.getNota(), status, linkAvaliacao,
                resultado.isAprovado() ? String.format("<p><a href=\"%s\">Clique aqui para imprimir seu certificado</a></p>", linkCertificado) : ""));

            messageProducer.sendMessage(mail);
        }

        if (telefone != null && !telefone.isBlank()) {
            String numero = telefone.replaceAll("[^0-9]", "");
            if (!numero.startsWith("55")) {
                numero = "55" + numero;
            }

            String mensagem = String.format("""
                Olá %s!

                Você concluiu a avaliação do curso gratuito.
                Nota: %.2f
                Status: %s

                📄 Avaliação: %s
                %s

                Equipe JB Segurança do Trabalho
            """, nome, resultado.getNota(), status, linkAvaliacao,
                resultado.isAprovado() ? "🎓 Certificado: " + linkCertificado : "");

            SendMessageZapDTO zap = new SendMessageZapDTO();
            zap.setPhone(numero);
            zap.setMessage(mensagem);

            zapSender.sendMessage(zap);
        }
    }

    private MatriculasGratuita buscarMatricula(UUID id) {
        return matriculasGratuitasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula gratuita não encontrada."));
    }

    private AvaliacaoCurso buscarAvaliacao(UUID id) {
        return avaliacaoCursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada."));
    }

    private Pergunta encontrarPerguntaPorId(List<Pergunta> perguntas, UUID idPergunta) {
        return perguntas.stream()
                .filter(p -> p.getIdPergunta().equals(idPergunta))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada: " + idPergunta));
    }

    private RespostaEad encontrarRespostaPorId(Pergunta pergunta, UUID idResposta) {
        return pergunta.getRespostas().stream()
                .filter(r -> r.getIdResposta().equals(idResposta))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada: " + idResposta));
    }

    private RespostaSelecionadaGratuita criarRespostaSelecionada(ResultadoAvaliacaoAlunoGratuito resultado, AvaliacaoCurso avaliacao, Pergunta pergunta, RespostaEad resposta) {
        RespostaSelecionadaGratuita rs = new RespostaSelecionadaGratuita();
        rs.setResultadoAvaliacao(resultado);
        rs.setPergunta(pergunta);
        rs.setRespostaEad(resposta);
        rs.setAvaliacao(avaliacao);
        return rs;
    }

    private double calcularNota(int acertos, int totalPerguntas) {
        return (10.0 * acertos) / totalPerguntas;
    }

    private ResultadoAvaliacaoGratuitaDto criarDtoResposta(ResultadoAvaliacaoAlunoGratuito resultado) {
        ResultadoAvaliacaoGratuitaDto dto = new ResultadoAvaliacaoGratuitaDto();
        dto.setNota(resultado.getNota());
        dto.setAprovado(resultado.isAprovado());
        dto.setDataResposta(resultado.getDataResposta());
        return dto;
    }
}