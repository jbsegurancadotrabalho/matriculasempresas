package br.com.jbst.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DTO.ResultadoAvaliacaoGratuitaDto;
import br.com.jbst.DtoEAD.PostRespostaResultadoDto;
import br.com.jbst.services.ResultadoAvaliacaoAlunoGratuitoService;


@RestController
@RequestMapping("/avaliacoes/gratuita/matricula")
public class ResultadoAvaliacaoAlunoGratuitoController {

	  @Autowired ResultadoAvaliacaoAlunoGratuitoService resultadoService;

	  
    @PostMapping("/responder-avaliacao-gratuita-curso/{idMatriculaGratuita}")
    public ResponseEntity<ResultadoAvaliacaoGratuitaDto> responderAvaliacaoGratuita(
            @PathVariable UUID idMatriculaGratuita,
            @RequestBody PostRespostaResultadoDto dto) {
        ResultadoAvaliacaoGratuitaDto resultado = resultadoService.responderAvaliacaoGratuita(dto, idMatriculaGratuita);
        return ResponseEntity.ok(resultado);
    }
    
}
