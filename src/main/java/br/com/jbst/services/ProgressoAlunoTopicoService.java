package br.com.jbst.services;

import br.com.jbst.DtoEAD.GetProgressoAlunoTopicoDto;

import br.com.jbst.DtoEAD.PostProgressoAlunoTopicoDto;
import br.com.jbst.DtoEAD.ResumoProgressoCursoDto;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.ProgressoAlunoTopico;
import br.com.jbst.entities.Topico;
import br.com.jbst.repositories.MatriculasRepository;
import br.com.jbst.repositoriesEAD.ProgressoAlunoTopicoRepository;
import br.com.jbst.repositoriesEAD.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressoAlunoTopicoService {

    private final ProgressoAlunoTopicoRepository progressoRepository;
    private final TopicoRepository topicoRepository;
    private final MatriculasRepository matriculasRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public GetProgressoAlunoTopicoDto registrarProgresso(PostProgressoAlunoTopicoDto dto) {
        
        // Validação explícita dos IDs
        if (dto.getIdMatricula() == null || dto.getIdTopico() == null) {
            throw new IllegalArgumentException("IDs da matrícula e tópico são obrigatórios");
        }

        // Busca entidades relacionadas
        Topico topico = topicoRepository.findById(dto.getIdTopico())
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));

        Matriculas matricula = matriculasRepository.findById(dto.getIdMatricula())
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada"));

        // Busca ou cria novo progresso
        ProgressoAlunoTopico progresso = progressoRepository
                .findByMatriculaAndTopico(matricula, topico)
                .orElseGet(() -> {
                    ProgressoAlunoTopico novo = new ProgressoAlunoTopico();
                    novo.setIdProgresso(UUID.randomUUID());
                    novo.setMatricula(matricula);
                    novo.setTopico(topico);
                    return novo;
                });

        // Atualiza estado
        progresso.setConcluido(dto.isConcluido());
        progresso.setDataConclusao(dto.isConcluido() ? Instant.now() : null);

        // Persiste no banco
        ProgressoAlunoTopico salvo = progressoRepository.save(progresso);

        return modelMapper.map(salvo, GetProgressoAlunoTopicoDto.class);
    }

    public List<GetProgressoAlunoTopicoDto> listarPorAluno(UUID idMatricula) {
        Matriculas matricula = matriculasRepository.findById(idMatricula)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada"));

        return progressoRepository.findByMatricula(matricula).stream()
                .map(p -> modelMapper.map(p, GetProgressoAlunoTopicoDto.class))
                .collect(Collectors.toList());
    }

    public ResumoProgressoCursoDto calcularResumo(UUID idMatricula, UUID idCurso) {
        List<Topico> topicosCurso = topicoRepository.findAll().stream()
                .filter(t -> t.getModulo().getCurso().getIdcurso().equals(idCurso))
                .collect(Collectors.toList());

        int totalTopicos = topicosCurso.size();

        Matriculas matricula = matriculasRepository.findById(idMatricula)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada"));

        long topicosConcluidos = progressoRepository.findByMatricula(matricula).stream()
                .filter(ProgressoAlunoTopico::isConcluido)
                .filter(p -> topicosCurso.stream()
                        .anyMatch(t -> t.getIdTopico().equals(p.getTopico().getIdTopico())))
                .count();

        ResumoProgressoCursoDto resumo = new ResumoProgressoCursoDto();
        resumo.setTotalTopicos(totalTopicos);
        resumo.setTopicosConcluidos((int) topicosConcluidos);
        resumo.setPercentualConcluido(totalTopicos == 0 ? 0.0 : (double) topicosConcluidos / totalTopicos * 100);

        return resumo;
    }
}