package br.com.jbst.services;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.jbst.DtoEAD.GetPerguntaTopicoDto;
import br.com.jbst.DtoEAD.PostPerguntaTopicoDto;
import br.com.jbst.DtoEAD.PutPerguntaTopicoDto;
import br.com.jbst.entities.Pergunta;
import br.com.jbst.entities.Topico;
import br.com.jbst.repositoriesEAD.PerguntaRepository;
import br.com.jbst.repositoriesEAD.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerguntaTopicoService {

    private final PerguntaRepository perguntaRepository;
    private final TopicoRepository topicoRepository;
    private final ModelMapper modelMapper;

    // Criar pergunta vinculada a um tópico
    public GetPerguntaTopicoDto criarPergunta(PostPerguntaTopicoDto dto) {
        Topico topico = topicoRepository.findById(dto.getIdTopico())
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));

        Pergunta pergunta = new Pergunta();
        pergunta.setIdPergunta(UUID.randomUUID());
        pergunta.setEnunciado(dto.getEnunciado());
        pergunta.setTopico(topico);

        Pergunta salva = perguntaRepository.save(pergunta);
        return modelMapper.map(salva, GetPerguntaTopicoDto.class);
    }

    // Atualizar pergunta existente
    public GetPerguntaTopicoDto atualizarPergunta(PutPerguntaTopicoDto dto) {
        Pergunta pergunta = perguntaRepository.findById(dto.getIdPergunta())
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));

        pergunta.setEnunciado(dto.getEnunciado());
        Pergunta atualizada = perguntaRepository.save(pergunta);

        return modelMapper.map(atualizada, GetPerguntaTopicoDto.class);
    }

    // Listar perguntas de um tópico
    public List<GetPerguntaTopicoDto> listarPorTopico(UUID idTopico) {
        List<Pergunta> perguntas = perguntaRepository.findByTopicoIdTopico(idTopico);
        return perguntas.stream()
                .map(p -> modelMapper.map(p, GetPerguntaTopicoDto.class))
                .collect(Collectors.toList());
    }

    // Buscar uma pergunta por ID
    public GetPerguntaTopicoDto buscarPorId(UUID idPergunta) {
        Pergunta pergunta = perguntaRepository.findById(idPergunta)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));
        return modelMapper.map(pergunta, GetPerguntaTopicoDto.class);
    }

    // Deletar uma pergunta
    public void deletar(UUID idPergunta) {
        Pergunta pergunta = perguntaRepository.findById(idPergunta)
                .orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));
        perguntaRepository.delete(pergunta);
    }
}
