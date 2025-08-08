package br.com.jbst.controller;

import br.com.jbst.DtoEAD.GetProgressoAlunoTopicoDto;
import br.com.jbst.DtoEAD.PostProgressoAlunoTopicoDto;
import br.com.jbst.DtoEAD.ResumoProgressoCursoDto;
import br.com.jbst.services.ProgressoAlunoTopicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/progresso")
@RequiredArgsConstructor
public class ProgressoAlunoTopicoController {

    private final ProgressoAlunoTopicoService progressoService;

    @PostMapping
    public ResponseEntity<GetProgressoAlunoTopicoDto> registrarProgresso(@RequestBody @Valid PostProgressoAlunoTopicoDto dto) {
        GetProgressoAlunoTopicoDto progressoRegistrado = progressoService.registrarProgresso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(progressoRegistrado);
    }

    @GetMapping("/aluno/{idMatricula}")
    public ResponseEntity<List<GetProgressoAlunoTopicoDto>> listarProgressoPorAluno(@PathVariable UUID idMatricula) {
        List<GetProgressoAlunoTopicoDto> progressos = progressoService.listarPorAluno(idMatricula);
        return ResponseEntity.ok(progressos);
    }

    @GetMapping("/resumo/aluno/{idMatricula}/curso/{idCurso}")
    public ResponseEntity<ResumoProgressoCursoDto> calcularResumoProgresso(
            @PathVariable UUID idMatricula,
            @PathVariable UUID idCurso) {
        ResumoProgressoCursoDto resumo = progressoService.calcularResumo(idMatricula, idCurso);
        return ResponseEntity.ok(resumo);
    }
}