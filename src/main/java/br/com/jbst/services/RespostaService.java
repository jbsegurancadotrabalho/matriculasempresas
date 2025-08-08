package br.com.jbst.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DtoEAD.GetRespostaDto;
import br.com.jbst.DtoEAD.PostRespostaDto;
import br.com.jbst.DtoEAD.PutRespostaDto;
import br.com.jbst.entities.Pergunta;
import br.com.jbst.entities.RespostaEad;
import br.com.jbst.repositoriesEAD.PerguntaRepository;
import br.com.jbst.repositoriesEAD.RespostaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RespostaService {

	@Autowired
	RespostaRepository respostaRepository;
	@Autowired
	PerguntaRepository perguntaRepository;
	@Autowired
	ModelMapper modelMapper;

	public GetRespostaDto criarResposta(PostRespostaDto dto) {
		Pergunta pergunta = perguntaRepository.findById(dto.getIdPergunta())
				.orElseThrow(() -> new EntityNotFoundException("Pergunta não encontrada"));

		RespostaEad resposta = modelMapper.map(dto, RespostaEad.class);
		resposta.setIdResposta(UUID.randomUUID()); // ← ESSENCIAL!
		resposta.setDescricao(dto.getDescricao());
		resposta.setCorreta(dto.isCorreta());
		resposta.setPergunta(pergunta);

		RespostaEad salvo = respostaRepository.save(resposta);
		return modelMapper.map(salvo, GetRespostaDto.class);
	}

	// Atualizar uma resposta existente
	public GetRespostaDto atualizarResposta(UUID idResposta, PutRespostaDto dto) {
		RespostaEad resposta = respostaRepository.findById(idResposta)
				.orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));

		resposta.setDescricao(dto.getDescricao());
		resposta.setCorreta(dto.isCorreta());

		RespostaEad atualizada = respostaRepository.save(resposta);
		return modelMapper.map(atualizada, GetRespostaDto.class);
	}

	// Buscar resposta por ID
	public GetRespostaDto buscarPorId(UUID idResposta) {
		RespostaEad resposta = respostaRepository.findById(idResposta)
				.orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));
		return modelMapper.map(resposta, GetRespostaDto.class);
	}

	// Listar todas as respostas
	public List<GetRespostaDto> listarTodas() {
		return respostaRepository.findAll().stream().map(resposta -> modelMapper.map(resposta, GetRespostaDto.class))
				.collect(Collectors.toList());
	}

	// Deletar resposta por ID
	public void deletar(UUID idResposta) {
		if (!respostaRepository.existsById(idResposta)) {
			throw new EntityNotFoundException("Resposta não encontrada");
		}
		respostaRepository.deleteById(idResposta);
	}
	
	public List<GetRespostaDto> listarPorPergunta(UUID idPergunta) {
        List<RespostaEad> respostas = respostaRepository.findByPergunta_IdPergunta(idPergunta);
        return respostas.stream()
                .map(resposta -> modelMapper.map(resposta, GetRespostaDto.class))
                .collect(Collectors.toList());
    }
}
