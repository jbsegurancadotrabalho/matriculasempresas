package br.com.jbst.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DtoEAD.GetResultadoDto;
import br.com.jbst.DtoEAD.PostRespostaResultadoDto;
import br.com.jbst.DtoEAD.ResultadoAvaliacaoDto;
import br.com.jbst.entities.ResultadoAvaliacaoAluno;
import br.com.jbst.services.ResultadoAvaliacaoAlunoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/avaliacoes/matricula")
public class ResultadoAvaliacaoAlunoController {

    private final ResultadoAvaliacaoAlunoService resultadoService;

    public ResultadoAvaliacaoAlunoController(ResultadoAvaliacaoAlunoService resultadoService) {
        this.resultadoService = resultadoService;
    }

   
    @PostMapping("/responder/{idMatricula}")
    public ResponseEntity<ResultadoAvaliacaoDto> responderAvaliacao(
            @RequestBody @Valid PostRespostaResultadoDto dto,
            @PathVariable UUID idMatricula,
            HttpServletRequest request) {
        
        try {
            ResultadoAvaliacaoDto resultado = resultadoService.responderAvaliacao(dto, idMatricula);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    


    

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    

    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<List<GetResultadoDto>> buscarPorMatricula(@PathVariable UUID idMatricula) {
        List<GetResultadoDto> resultados = resultadoService.buscarPorIdMatricula(idMatricula);
        return ResponseEntity.ok(resultados);
    }

    // GET: listar resultados de avaliações por matrícula
    @GetMapping("/resultados/{idMatricula}")
    public ResponseEntity<List<ResultadoAvaliacaoAluno>> listarResultados(@PathVariable UUID idMatricula) {
        List<ResultadoAvaliacaoAluno> resultados = resultadoService.listarResultadosPorMatricula(idMatricula);
        return ResponseEntity.ok(resultados);
    }
} 
