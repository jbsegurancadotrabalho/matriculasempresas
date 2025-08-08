package br.com.jbst.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DtoEAD.GetAvaliacaoCursoDto;
import br.com.jbst.DtoEAD.PostAvaliacaoCursoDto;
import br.com.jbst.DtoEAD.PutAvaliacaoCursoDto;
import br.com.jbst.services.AvaliacaoCursoService;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoCursoControllers {

    @Autowired AvaliacaoCursoService avaliacaoCursoService;

   

    @PostMapping
    public ResponseEntity<GetAvaliacaoCursoDto> criar(@RequestBody PostAvaliacaoCursoDto dto) {
        GetAvaliacaoCursoDto nova = avaliacaoCursoService.criarAvaliacao(dto);
        return ResponseEntity.ok(nova);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetAvaliacaoCursoDto> atualizar(
            @PathVariable UUID id,
            @RequestBody PutAvaliacaoCursoDto dto) {
        GetAvaliacaoCursoDto atualizada = avaliacaoCursoService.atualizarAvaliacao(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAvaliacaoCursoDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(avaliacaoCursoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<GetAvaliacaoCursoDto>> listarTodas() {
        return ResponseEntity.ok(avaliacaoCursoService.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        avaliacaoCursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
   
}

