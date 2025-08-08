package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jbst.DtoEAD.GetProgressoAlunoTopicoGratuitoDto;
import br.com.jbst.DtoEAD.PostProgressoAlunoTopicoGratuitoDto;
import br.com.jbst.DtoEAD.ResumoProgressoCursoGratuitoDto;
import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DTO.SendMessageZapDTO;
import br.com.jbst.components.MatriculasMessageProducer;
import br.com.jbst.components.ZApiSenderComponent;
import br.com.jbst.entities.MatriculasGratuita;
import br.com.jbst.entities.Modulo;
import br.com.jbst.entities.ProgressoAlunoTopicoGratuito;
import br.com.jbst.entities.Topico;
import br.com.jbst.repositories.MatriculaGratuitaRepository;
import br.com.jbst.repositoriesEAD.ProgressoAlunoTopicoGratuitoRepository;
import br.com.jbst.repositoriesEAD.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressoAlunoTopicoGratuitoService {

    private final ProgressoAlunoTopicoGratuitoRepository progressoRepository;
    private final TopicoRepository topicoRepository;
    private final MatriculaGratuitaRepository matriculasRepository;
    private final ModelMapper modelMapper;
    private final MatriculasMessageProducer matriculasMessageProducer;
    private final ZApiSenderComponent zApiSenderComponent;

    @Transactional
    public GetProgressoAlunoTopicoGratuitoDto registrarProgresso(PostProgressoAlunoTopicoGratuitoDto dto) {

        if (dto.getIdMatriculaGratuita() == null || dto.getIdTopico() == null) {
            throw new IllegalArgumentException("IDs da matrícula e do tópico são obrigatórios.");
        }

        Topico topico = topicoRepository.findById(dto.getIdTopico())
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));

        MatriculasGratuita matricula = matriculasRepository.findById(dto.getIdMatriculaGratuita())
                .orElseThrow(() -> new EntityNotFoundException("Matrícula gratuita não encontrada"));

        ProgressoAlunoTopicoGratuito progresso = progressoRepository
                .findByMatriculasGratuitaAndTopico(matricula, topico)
                .orElseGet(() -> {
                    ProgressoAlunoTopicoGratuito novo = new ProgressoAlunoTopicoGratuito();
                    novo.setIdProgresso(UUID.randomUUID());
                    novo.setMatriculasGratuita(matricula);
                    novo.setTopico(topico);
                    return novo;
                });

        progresso.setConcluido(dto.isConcluido());
        progresso.setDataConclusao(dto.isConcluido() ? Instant.now() : null);

        ProgressoAlunoTopicoGratuito salvo = progressoRepository.save(progresso);

        if (dto.isConcluido()) {
            enviarFeedbackConclusao(matricula, topico);
        }

        return modelMapper.map(salvo, GetProgressoAlunoTopicoGratuitoDto.class);
    }

    private void enviarFeedbackConclusao(MatriculasGratuita matricula, Topico topicoConcluido) {
        Modulo modulo = topicoConcluido.getModulo();
        UUID idCurso = modulo.getCurso().getIdcurso();

        List<Topico> todosTopicosCurso = topicoRepository.findAll().stream()
                .filter(t -> t.getModulo().getCurso().getIdcurso().equals(idCurso))
                .collect(Collectors.toList());

        List<Modulo> todosModulosCurso = todosTopicosCurso.stream()
                .map(Topico::getModulo)
                .distinct()
                .collect(Collectors.toList());

        List<ProgressoAlunoTopicoGratuito> concluidos = progressoRepository.findByMatriculasGratuita(matricula).stream()
                .filter(ProgressoAlunoTopicoGratuito::isConcluido)
                .toList();

        long topicosConcluidos = concluidos.stream()
                .filter(p -> todosTopicosCurso.stream().anyMatch(t -> t.getIdTopico().equals(p.getTopico().getIdTopico())))
                .count();

        long modulosConcluidos = todosModulosCurso.stream()
                .filter(m -> m.getTopicos().stream().allMatch(t ->
                        concluidos.stream().anyMatch(p -> p.getTopico().getIdTopico().equals(t.getIdTopico()))
                )).count();

        int topicosRestantes = todosTopicosCurso.size() - (int) topicosConcluidos;
        int modulosRestantes = todosModulosCurso.size() - (int) modulosConcluidos;

        String email = matricula.getEmail();
        String telefone = matricula.getWhatsapp();
        String tituloTopico = topicoConcluido.getTitulo();
        String tituloModulo = modulo.getTitulo();

        if (email != null && !email.isBlank()) {
            MailSenderDto mailDto = new MailSenderDto();
            mailDto.setMailTo(email);
            mailDto.setSubject("Você concluiu um novo tópico!");
            mailDto.setBody(String.format("""
                <p>Parabéns!</p>
                <p>Você concluiu o tópico <strong>%s</strong> do módulo <strong>%s</strong>.</p>
                <p>Restam <strong>%d tópicos</strong> e <strong>%d módulos</strong> para concluir o curso.</p>
                <p>Continue assim!</p>
            """, tituloTopico, tituloModulo, topicosRestantes, modulosRestantes));

            matriculasMessageProducer.sendMessage(mailDto);
        }

        if (telefone != null && !telefone.isBlank()) {
            String numero = telefone.replaceAll("[^0-9]", "");
            if (!numero.startsWith("55")) {
                numero = "55" + numero;
            }

            SendMessageZapDTO zapDto = new SendMessageZapDTO();
            zapDto.setPhone(numero);
            zapDto.setMessage(String.format("""
                👏 Parabéns!

                Você concluiu o tópico *%s* do módulo *%s*.

                📚 Faltam %d tópicos e %d módulos para você concluir o curso.

                Continue firme! 💪
            """, tituloTopico, tituloModulo, topicosRestantes, modulosRestantes));

            zApiSenderComponent.sendMessage(zapDto);
        }
    }

    public List<GetProgressoAlunoTopicoGratuitoDto> listarPorAluno(UUID idMatriculaGratuita) {
        MatriculasGratuita matricula = matriculasRepository.findById(idMatriculaGratuita)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula gratuita não encontrada"));

        return progressoRepository.findByMatriculasGratuita(matricula).stream()
                .map(p -> modelMapper.map(p, GetProgressoAlunoTopicoGratuitoDto.class))
                .collect(Collectors.toList());
    }

    public ResumoProgressoCursoGratuitoDto calcularResumo(UUID idMatriculaGratuita, UUID idCurso) {
        MatriculasGratuita matricula = matriculasRepository.findById(idMatriculaGratuita)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula gratuita não encontrada"));

        List<Topico> topicosCurso = topicoRepository.findAll().stream()
                .filter(t -> t.getModulo().getCurso().getIdcurso().equals(idCurso))
                .collect(Collectors.toList());

        int totalTopicos = topicosCurso.size();

        long topicosConcluidos = progressoRepository.findByMatriculasGratuita(matricula).stream()
                .filter(ProgressoAlunoTopicoGratuito::isConcluido)
                .filter(p -> topicosCurso.stream()
                        .anyMatch(t -> t.getIdTopico().equals(p.getTopico().getIdTopico())))
                .count();

        ResumoProgressoCursoGratuitoDto resumo = new ResumoProgressoCursoGratuitoDto();
        resumo.setTotalTopicos(totalTopicos);
        resumo.setTopicosConcluidos((int) topicosConcluidos);
        resumo.setPercentualConcluido(totalTopicos == 0 ? 0.0 : ((double) topicosConcluidos / totalTopicos) * 100.0);

        return resumo;
    }
}