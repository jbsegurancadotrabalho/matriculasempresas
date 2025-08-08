package br.com.jbst.services;

import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetEmpresaDTO;
import br.com.jbst.DTO.GetFuncaoDTO;
import br.com.jbst.DTO.GetFuncionarioDTO;
import br.com.jbst.DTO.GetMatriculasDTO;
import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DtoEAD.GetAvaliacaoCursoDto;
import br.com.jbst.DtoEAD.GetPerguntaDto;
import br.com.jbst.DtoEAD.GetRespostaDto;
import br.com.jbst.DtoEAD.GetRespostaSelecionadaDto;
import br.com.jbst.DtoEAD.GetResultadoDto;
import br.com.jbst.DtoEAD.PostRespostaResultadoDto;
import br.com.jbst.DtoEAD.ResultadoAvaliacaoDto;
import br.com.jbst.entities.AvaliacaoCurso;
import br.com.jbst.entities.MatriculasResultados;
import br.com.jbst.entities.Pergunta;
import br.com.jbst.entities.RespostaEad;
import br.com.jbst.entities.RespostaSelecionada;
import br.com.jbst.entities.ResultadoAvaliacaoAluno;
import br.com.jbst.entities.ResultadoAvaliacaoAlunoGratuito;
import br.com.jbst.repositories.MatriculaGratuitaRepository;
import br.com.jbst.repositoriesEAD.AvaliacaoCursoRepository;
import br.com.jbst.repositoriesEAD.MatriculasResultadosRepository;
import br.com.jbst.repositoriesEAD.PerguntaRepository;
import br.com.jbst.repositoriesEAD.ResultadoAvaliacaoAlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultadoAvaliacaoAlunoService {

    @Autowired ResultadoAvaliacaoAlunoRepository resultadoRepository;
    @Autowired AvaliacaoCursoRepository avaliacaoCursoRepository;
    @Autowired PerguntaRepository perguntaRepository;
    @Autowired MatriculasResultadosRepository matriculasRepository;
    @Autowired MatriculaGratuitaRepository matriculasGratuitasRepository;
    @Autowired private ModelMapper modelMapper;
    
    public ResultadoAvaliacaoDto responderAvaliacao(PostRespostaResultadoDto dto, UUID idMatricula) {
        MatriculasResultados matricula = matriculasRepository.findById(idMatricula)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada."));
        AvaliacaoCurso avaliacao = avaliacaoCursoRepository.findById(dto.getIdAvaliacaoCurso())
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada."));
        List<Pergunta> perguntas = perguntaRepository.findByAvaliacao_IdAvaliacaoCurso(dto.getIdAvaliacaoCurso());
        ResultadoAvaliacaoAluno resultado = new ResultadoAvaliacaoAluno();
        resultado.setIdResultado(UUID.randomUUID());
        resultado.setMatricula(matricula);
        resultado.setAvaliacao(avaliacao);
        resultado.setDataResposta(Instant.now());
        int acertos = 0;
        List<RespostaSelecionada> respostasSelecionadas = new ArrayList<>();
        for (PostRespostaResultadoDto.RespostaDto respostaDto : dto.getRespostas()) {
            Pergunta pergunta = perguntas.stream()
                    .filter(p -> p.getIdPergunta().equals(respostaDto.getIdPergunta()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Pergunta não encontrada: " + respostaDto.getIdPergunta()));
            RespostaEad respostaSelecionada = pergunta.getRespostas().stream()
                    .filter(r -> r.getIdResposta().equals(respostaDto.getIdResposta()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Resposta não encontrada: " + respostaDto.getIdResposta()));
            if (respostaSelecionada.isCorreta()) {
                acertos++;
            }
            RespostaSelecionada rs = new RespostaSelecionada();
            rs.setResultado(resultado);
            rs.setPergunta(pergunta);
            rs.setRespostaEad(respostaSelecionada);
            rs.setAvaliacao(avaliacao);
            respostasSelecionadas.add(rs);
        }
        double nota = (10.0 * acertos) / perguntas.size();
        boolean aprovado = nota >= 7.0;
        resultado.setNota(nota);
        resultado.setAprovado(aprovado);
        resultado.setRespostasSelecionadas(respostasSelecionadas);
        resultadoRepository.save(resultado);
        ResultadoAvaliacaoDto resposta = new ResultadoAvaliacaoDto();
        resposta.setNota(nota);
        resposta.setAprovado(aprovado);
        resposta.setDataResposta(resultado.getDataResposta());
        return resposta;
    }
    
    
   
    

    public List<GetResultadoDto> buscarPorIdMatricula(UUID idMatricula) {
        List<ResultadoAvaliacaoAluno> resultados = resultadoRepository.findByMatriculaIdMatriculaComRespostas(idMatricula);

        return resultados.stream().map(resultado -> {
            GetResultadoDto dto = new GetResultadoDto();
            dto.setIdResultado(resultado.getIdResultado());
            dto.setNomeAvaliacao(resultado.getAvaliacao().getNomeAvaliacao());
            dto.setNota(resultado.getNota());
            dto.setAprovado(resultado.isAprovado());
            dto.setDataResposta(resultado.getDataResposta());

            // 📦 Mapear matrícula
            MatriculasResultados matricula = resultado.getMatricula(); // <- getter correto
            if (matricula != null) {
                GetMatriculasDTO matriculaDto = modelMapper.map(matricula, GetMatriculasDTO.class);

                // Funcionario
                if (matricula.getFuncionario() != null) {
                    GetFuncionarioDTO funcionarioDto = modelMapper.map(matricula.getFuncionario(), GetFuncionarioDTO.class);

                    if (matricula.getFuncionario().getEmpresa() != null) {
                        GetEmpresaDTO empresaDto = modelMapper.map(matricula.getFuncionario().getEmpresa(), GetEmpresaDTO.class);
                        funcionarioDto.setEmpresa(empresaDto);
                    }

                    if (matricula.getFuncionario().getFuncao() != null) {
                        GetFuncaoDTO funcaoDto = modelMapper.map(matricula.getFuncionario().getFuncao(), GetFuncaoDTO.class);
                        funcionarioDto.setFuncao(funcaoDto);
                    }

                    matriculaDto.setFuncionario(funcionarioDto);
                }

                // Turma
                if (matricula.getTurmas() != null) {
                    GetTurmasDTO turmasDto = modelMapper.map(matricula.getTurmas(), GetTurmasDTO.class);
                    matriculaDto.setTurmas(turmasDto);
                }

                dto.setMatricula(matriculaDto);
            }

            // ✅ Mapear respostas selecionadas
            List<GetRespostaSelecionadaDto> respostasSelecionadasDto = new ArrayList<>();
            if (resultado.getRespostasSelecionadas() != null) {
                for (RespostaSelecionada rs : resultado.getRespostasSelecionadas()) {
                    GetRespostaSelecionadaDto respostaDto = new GetRespostaSelecionadaDto();
                    respostaDto.setIdRespostaSelecionada(rs.getIdRespostaSelecionada());

                    if (rs.getPergunta() != null) {
                        respostaDto.setIdPergunta(rs.getPergunta().getIdPergunta());
                        respostaDto.setTituloPergunta(rs.getPergunta().getEnunciado());
                    }

                    if (rs.getRespostaEad() != null) {
                        respostaDto.setIdResposta(rs.getRespostaEad().getIdResposta());
                        respostaDto.setDescricaoResposta(rs.getRespostaEad().getDescricao());
                        respostaDto.setRespostaCorreta(rs.getRespostaEad().isCorreta());
                    }

                    respostasSelecionadasDto.add(respostaDto);
                }
            }

            dto.setRespostasSelecionadas(respostasSelecionadasDto);

            // ✅ Mapear avaliação
            AvaliacaoCurso avaliacao = resultado.getAvaliacao();
            GetAvaliacaoCursoDto avaliacaoDto = new GetAvaliacaoCursoDto();
            avaliacaoDto.setIdAvaliacaoCurso(avaliacao.getIdAvaliacaoCurso());
            avaliacaoDto.setNomeAvaliacao(avaliacao.getNomeAvaliacao());
            avaliacaoDto.setDescricaoAvaliacao(avaliacao.getDescricaoAvaliacao());
            avaliacaoDto.setNomeCurso(avaliacao.getCurso().getCurso());

            // Perguntas + Respostas
            List<GetPerguntaDto> perguntasDto = avaliacao.getPerguntas().stream().map(pergunta -> {
                GetPerguntaDto perguntaDto = modelMapper.map(pergunta, GetPerguntaDto.class);

                List<GetRespostaDto> respostasDto = pergunta.getRespostas().stream().map(resposta -> {
                    GetRespostaDto respostaDto = modelMapper.map(resposta, GetRespostaDto.class);

                    boolean foiSelecionada = resultado.getRespostasSelecionadas().stream().anyMatch(rs ->
                        rs.getPergunta() != null &&
                        rs.getRespostaEad() != null &&
                        rs.getPergunta().getIdPergunta().equals(pergunta.getIdPergunta()) &&
                        rs.getRespostaEad().getIdResposta().equals(resposta.getIdResposta())
                    );

                    respostaDto.setSelecionada(foiSelecionada);
                    return respostaDto;
                }).collect(Collectors.toList());

                perguntaDto.setRespostas(respostasDto);
                return perguntaDto;
            }).collect(Collectors.toList());

            avaliacaoDto.setPerguntas(perguntasDto);
            dto.setAvaliacao(avaliacaoDto);

            return dto;
        }).collect(Collectors.toList());
    }


    public List<ResultadoAvaliacaoAluno> listarResultadosPorMatricula(UUID idMatricula) {
        if (!matriculasRepository.existsById(idMatricula)) {
            throw new EntityNotFoundException("Matrícula não encontrada.");
        }

        return resultadoRepository.findByMatricula_IdMatricula(idMatricula);
    }
}
