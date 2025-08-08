package br.com.jbst.services;

import br.com.jbst.DtoEAD.*;
import br.com.jbst.entities.AvaliacaoCurso;
import br.com.jbst.entities.Pergunta;
import br.com.jbst.entities.RespostaEad;
import br.com.jbst.repositoriesEAD.AvaliacaoCursoRepository;
import br.com.jbst.repositoriesEAD.PerguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerguntaService {

    @Autowired
    private PerguntaRepository perguntaRepository;
    @Autowired
    private AvaliacaoCursoRepository avaliacaoCursoRepository;
    @Autowired
    private ModelMapper modelMapper;

    public GetPerguntaDto criarPergunta(PostPerguntaDto dto) {
        AvaliacaoCurso avaliacao = avaliacaoCursoRepository.findById(dto.getIdAvaliacaoCurso())
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

        Pergunta pergunta = new Pergunta();
        pergunta.setIdPergunta(UUID.randomUUID());
        pergunta.setEnunciado(dto.getEnunciado());
        pergunta.setAvaliacao(avaliacao);

        // Respostas
        List<RespostaEad> respostas = dto.getRespostas().stream().map(respostaDto -> {
            RespostaEad r = modelMapper.map(respostaDto, RespostaEad.class);
            r.setIdResposta(UUID.randomUUID());
            r.setPergunta(pergunta);
            return r;
        }).collect(Collectors.toList());

        pergunta.setRespostas(respostas);

        Pergunta salva = perguntaRepository.save(pergunta);
        return modelMapper.map(salva, GetPerguntaDto.class);
    }

    public GetPerguntaDto atualizarPergunta(UUID idPergunta, PutPerguntaDto dto) {
        Pergunta pergunta = perguntaRepository.findById(idPergunta)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));

        AvaliacaoCurso avaliacao = avaliacaoCursoRepository.findById(dto.getIdAvaliacao())
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

        pergunta.setEnunciado(dto.getEnunciado());
        pergunta.setAvaliacao(avaliacao);

        // Substituir todas as respostas
        pergunta.getRespostas().clear();
        List<RespostaEad> novasRespostas = dto.getRespostas().stream().map(respostaDto -> {
            RespostaEad r = modelMapper.map(respostaDto, RespostaEad.class);
            r.setIdResposta(UUID.randomUUID());
            r.setPergunta(pergunta);
            return r;
        }).collect(Collectors.toList());

        pergunta.getRespostas().addAll(novasRespostas);

        Pergunta atualizada = perguntaRepository.save(pergunta);
        return modelMapper.map(atualizada, GetPerguntaDto.class);
    }

    public GetPerguntaDto buscarPorId(UUID idPergunta) {
        Pergunta pergunta = perguntaRepository.findById(idPergunta)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));
        return modelMapper.map(pergunta, GetPerguntaDto.class);
    }

    public List<GetPerguntaDto> listarTodas() {
        return perguntaRepository.findAll().stream()
                .map(pergunta -> modelMapper.map(pergunta, GetPerguntaDto.class))
                .collect(Collectors.toList());
    }

    public void deletar(UUID idPergunta) {
        if (!perguntaRepository.existsById(idPergunta)) {
            throw new EntityNotFoundException("Pergunta não encontrada");
        }
        perguntaRepository.deleteById(idPergunta);
    }
}
