package br.com.jbst.services;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DtoEAD.GetRespostaSelecionadaDto;
import br.com.jbst.entities.ResultadoAvaliacaoAluno;
import br.com.jbst.repositoriesEAD.RespostaSelecionadaRepository;
import br.com.jbst.repositoriesEAD.ResultadoAvaliacaoAlunoRepository;

@Service
public class RespostaSelecionadaService {

    @Autowired RespostaSelecionadaRepository respostaSelecionadaRepository;
    @Autowired ResultadoAvaliacaoAlunoRepository resultadoRepository;


    public List<GetRespostaSelecionadaDto> buscarUltimasRespostasSelecionadas() {
        List<ResultadoAvaliacaoAluno> resultados = resultadoRepository.findAll();

        return resultados.stream()
            .flatMap(resultado -> resultado.getRespostasSelecionadas().stream()
                .map(rs -> {
                    GetRespostaSelecionadaDto dto = new GetRespostaSelecionadaDto();
                    dto.setIdRespostaSelecionada(rs.getIdRespostaSelecionada());

                    if (rs.getPergunta() != null) {
                        dto.setIdPergunta(rs.getPergunta().getIdPergunta());
                        dto.setTituloPergunta(rs.getPergunta().getEnunciado()); // Corrigido
                    }

                    if (rs.getRespostaEad() != null) {
                        dto.setIdResposta(rs.getRespostaEad().getIdResposta()); // Corrigido
                        dto.setDescricaoResposta(rs.getRespostaEad().getDescricao());
                        dto.setRespostaCorreta(rs.getRespostaEad().isCorreta()); // <-- Aqui!
                    }

                    return dto;
                })
            )
            .collect(Collectors.toList());
    }

}
