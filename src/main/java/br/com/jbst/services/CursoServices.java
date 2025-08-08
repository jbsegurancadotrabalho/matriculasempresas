package br.com.jbst.services;


import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DtoEAD.GetCursoDto;
import br.com.jbst.entities.Curso;
import br.com.jbst.repositories.CursoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CursoServices {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public GetCursoDto buscarCursoPorId(UUID idcurso) {
        Curso curso = cursoRepository.findById(idcurso)
            .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + idcurso));

        return modelMapper.map(curso, GetCursoDto.class);
    }
}
