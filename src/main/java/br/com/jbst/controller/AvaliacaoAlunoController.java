package br.com.jbst.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DtoEAD.RespostaAlunoDto;
import br.com.jbst.DtoEAD.ResultadoAvaliacaoDto;
import br.com.jbst.services.AvaliacaoAlunoService;

@RestController
@RequestMapping("/avaliacoes/aluno")
public class AvaliacaoAlunoController {

    private final AvaliacaoAlunoService avaliacaoAlunoService;

    public AvaliacaoAlunoController(AvaliacaoAlunoService avaliacaoAlunoService) {
        this.avaliacaoAlunoService = avaliacaoAlunoService;
    }

    @PostMapping("/responder")
    public ResponseEntity<ResultadoAvaliacaoDto> responder(@RequestBody RespostaAlunoDto dto) {
        double nota = avaliacaoAlunoService.calcularNota(dto.getRespostasSelecionadas(), dto.getIdAvaliacaoCurso());
        boolean aprovado = avaliacaoAlunoService.isAprovado(nota);

        ResultadoAvaliacaoDto resultado = new ResultadoAvaliacaoDto();
        resultado.setNota(nota);
        resultado.setAprovado(aprovado);

        return ResponseEntity.ok(resultado);
    }
}
