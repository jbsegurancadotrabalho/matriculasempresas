package br.com.jbst.services;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DtoEAD.GetModuloDto;
import br.com.jbst.DtoEAD.PostModuloDto;
import br.com.jbst.DtoEAD.PutModuloDto;
import br.com.jbst.entities.Curso;
import br.com.jbst.entities.Modulo;
import br.com.jbst.repositories.CursoRepository;
import br.com.jbst.repositoriesEAD.ModuloRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ModuloService {

	 @Autowired ModuloRepository moduloRepository;
    @Autowired CursoRepository cursoRepository;
    @Autowired ModelMapper modelMapper;

    public GetModuloDto criarModulo(PostModuloDto dto) {
        Curso curso = cursoRepository.findById(dto.getIdcurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
        Modulo modulo = modelMapper.map(dto, Modulo.class);
        modulo.setIdModulo(UUID.randomUUID());
        modulo.setCurso(curso);
        Modulo salvo = moduloRepository.save(modulo);
        return modelMapper.map(salvo, GetModuloDto.class);
    }

    public GetModuloDto atualizarModulo(PutModuloDto dto, UUID id) {
        Modulo modulo = moduloRepository.findById(dto.getIdModulo())
            .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado"));

        // Mapeia os campos do DTO para a entidade existente
        modelMapper.map(dto, modulo);

        Modulo atualizado = moduloRepository.save(modulo);
        return modelMapper.map(atualizado, GetModuloDto.class);
    }

    public List<GetModuloDto> listarTodos() {
        List<Modulo> modulos = moduloRepository.findAll();
        return modulos.stream()
                .map(m -> modelMapper.map(m, GetModuloDto.class))
                .collect(Collectors.toList());
    }

    public GetModuloDto buscarPorId(UUID id) {
        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado"));
        return modelMapper.map(modulo, GetModuloDto.class);
    }

    public void deletar(UUID id) {
        if (!moduloRepository.existsById(id)) {
            throw new EntityNotFoundException("Módulo não encontrado");
        }
        moduloRepository.deleteById(id);
    }
}
