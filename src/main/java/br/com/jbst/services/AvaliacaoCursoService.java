package br.com.jbst.services;


import br.com.jbst.DtoEAD.GetAvaliacaoCursoDto;

import br.com.jbst.DtoEAD.PostAvaliacaoCursoDto;
import br.com.jbst.DtoEAD.PutAvaliacaoCursoDto;
import br.com.jbst.entities.AvaliacaoCurso;
import br.com.jbst.entities.Curso;
import br.com.jbst.entities.Funcao;
import br.com.jbst.repositories.CursoRepository;
import br.com.jbst.repositoriesEAD.AvaliacaoCursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvaliacaoCursoService {

	@Autowired AvaliacaoCursoRepository avaliacaoCursoRepository;
	@Autowired CursoRepository cursoRepository;
	@Autowired ModelMapper modelMapper;

    public GetAvaliacaoCursoDto criarAvaliacao(PostAvaliacaoCursoDto dto) {
        Curso curso = cursoRepository.findById(dto.getIdcurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        AvaliacaoCurso avaliacao = new AvaliacaoCurso();
        avaliacao.setIdAvaliacaoCurso(UUID.randomUUID());
        avaliacao.setCurso(curso);
        avaliacao.setNomeAvaliacao(dto.getNomeAvaliacao());
        avaliacao.setDescricaoAvaliacao(dto.getDescricaoAvaliacao());

        AvaliacaoCurso salva = avaliacaoCursoRepository.save(avaliacao);

        GetAvaliacaoCursoDto response = modelMapper.map(salva, GetAvaliacaoCursoDto.class);
        response.setNomeCurso(curso.getCurso());
        return response;
    }

    public GetAvaliacaoCursoDto atualizarAvaliacao(UUID id, PutAvaliacaoCursoDto dto) {
        AvaliacaoCurso avaliacao = avaliacaoCursoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

        // Mapeia as propriedades do DTO para a entidade existente
        modelMapper.map(dto, avaliacao);

        AvaliacaoCurso atualizada = avaliacaoCursoRepository.save(avaliacao);

        return modelMapper.map(atualizada, GetAvaliacaoCursoDto.class);
    }

    public GetAvaliacaoCursoDto buscarPorId(UUID id) {
        AvaliacaoCurso avaliacao = avaliacaoCursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

        GetAvaliacaoCursoDto dto = modelMapper.map(avaliacao, GetAvaliacaoCursoDto.class);
        dto.setNomeCurso(avaliacao.getCurso().getCurso());
        return dto;
    }

    public List<GetAvaliacaoCursoDto> listarTodas() {
        return avaliacaoCursoRepository.findAll().stream().map(av -> {
            GetAvaliacaoCursoDto dto = modelMapper.map(av, GetAvaliacaoCursoDto.class);
            if (av.getCurso() != null) {
                dto.setNomeCurso(av.getCurso().getCurso());
                dto.setIdcurso(av.getCurso().getIdcurso());
            }
            return dto;
        }).collect(Collectors.toList());
    }



    public void deletar(UUID id) {
        if (!avaliacaoCursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Avaliação não encontrada");
        }
        avaliacaoCursoRepository.deleteById(id);
    }
    
	
}
