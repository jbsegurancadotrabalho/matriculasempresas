package br.com.jbst.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.jbst.DtoEAD.GetProgressoAlunoTopicoGratuitoDto;
import br.com.jbst.DtoEAD.PostProgressoAlunoTopicoGratuitoDto;
import br.com.jbst.DtoEAD.ResumoProgressoCursoGratuitoDto;
import br.com.jbst.services.ProgressoAlunoTopicoGratuitoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/progresso-gratuito")
@RequiredArgsConstructor
public class ProgressoAlunoTopicoGratuitoController {

    private final ProgressoAlunoTopicoGratuitoService service;

    @PostMapping
    public ResponseEntity<GetProgressoAlunoTopicoGratuitoDto> registrarProgresso(
            @RequestBody PostProgressoAlunoTopicoGratuitoDto dto) {
        GetProgressoAlunoTopicoGratuitoDto progresso = service.registrarProgresso(dto);
        return ResponseEntity.ok(progresso);
    }
 
    @GetMapping("/{idMatriculaGratuita}")
    public ResponseEntity<List<GetProgressoAlunoTopicoGratuitoDto>> listarPorMatricula(
            @PathVariable UUID idMatriculaGratuita) {
        List<GetProgressoAlunoTopicoGratuitoDto> progresso = service.listarPorAluno(idMatriculaGratuita);
        return ResponseEntity.ok(progresso);
    }

   
    @GetMapping("/resumo")
    public ResponseEntity<ResumoProgressoCursoGratuitoDto> calcularResumo(
            @RequestParam UUID idMatriculaGratuita,
            @RequestParam UUID idCurso) {
        ResumoProgressoCursoGratuitoDto resumo = service.calcularResumo(idMatriculaGratuita, idCurso);
        return ResponseEntity.ok(resumo);
    }
}
