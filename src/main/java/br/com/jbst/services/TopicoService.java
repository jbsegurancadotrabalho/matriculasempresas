package br.com.jbst.services;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DtoEAD.GetTopicoDto;
import br.com.jbst.DtoEAD.PostTopicoDto;
import br.com.jbst.DtoEAD.PutTopicoDto;
import br.com.jbst.entities.Modulo;
import br.com.jbst.entities.Topico;
import br.com.jbst.repositoriesEAD.ModuloRepository;
import br.com.jbst.repositoriesEAD.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TopicoService {

   @Autowired TopicoRepository topicoRepository;
   @Autowired	 ModuloRepository moduloRepository;
   @Autowired ModelMapper modelMapper;

    public GetTopicoDto criarTopico(PostTopicoDto dto) {
        Modulo modulo = moduloRepository.findById(dto.getIdModulo())
                .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado"));

        Topico topico = modelMapper.map(dto, Topico.class);
        topico.setIdTopico(UUID.randomUUID());
        topico.setModulo(modulo);

        Topico salvo = topicoRepository.save(topico);
        return modelMapper.map(salvo, GetTopicoDto.class);
    }

    public GetTopicoDto atualizarTopico(UUID idTopico, PutTopicoDto dto) {
        Topico topico = topicoRepository.findById(idTopico)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        topico.setTitulo(dto.getTitulo());
        topico.setConteudo(dto.getConteudo());
        topico.setOrdem(dto.getOrdem());

        Topico atualizado = topicoRepository.save(topico);
        return modelMapper.map(atualizado, GetTopicoDto.class);
    }

    public List<GetTopicoDto> listarTopicos() {
        return topicoRepository.findAll().stream()
                .map(t -> modelMapper.map(t, GetTopicoDto.class))
                .collect(Collectors.toList());
    }

    public GetTopicoDto buscarPorId(UUID idTopico) {
        Topico topico = topicoRepository.findById(idTopico)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        return modelMapper.map(topico, GetTopicoDto.class);
    }

    public void deletarTopico(UUID idTopico) {
        if (!topicoRepository.existsById(idTopico)) {
            throw new EntityNotFoundException("Tópico não encontrado");
        }
        topicoRepository.deleteById(idTopico);
    }
}

