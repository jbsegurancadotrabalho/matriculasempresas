package br.com.jbst.controller;



import br.com.jbst.DTO.GetMatriculaGratuitaDTO;
import br.com.jbst.DTO.PostMatriculaGratuitaDTO;
import br.com.jbst.DTO.PutMatriculaGratuitaDTO;
import br.com.jbst.services.MatriculaGratuitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/matricula-gratuita")
public class MatriculasGratuitaController {

    @Autowired
    private MatriculaGratuitaService service;

    // POST - Criar nova matrícula gratuita
    @PostMapping
    public ResponseEntity<GetMatriculaGratuitaDTO> criar(@RequestBody PostMatriculaGratuitaDTO dto) {
        GetMatriculaGratuitaDTO result = service.criar(dto);
        return ResponseEntity.ok(result);
    }

    // PUT - Atualizar matrícula gratuita existente
    @PutMapping("/{id}")
    public ResponseEntity<GetMatriculaGratuitaDTO> atualizar(@PathVariable UUID id, @RequestBody PutMatriculaGratuitaDTO dto) {
        Optional<GetMatriculaGratuitaDTO> result = service.atualizar(id, dto);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET - Buscar matrícula gratuita por ID
    @GetMapping("/{id}")
    public ResponseEntity<GetMatriculaGratuitaDTO> buscarPorId(@PathVariable UUID id) {
        Optional<GetMatriculaGratuitaDTO> result = service.buscarPorId(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET - Listar todas as matrículas gratuitas
    @GetMapping
    public ResponseEntity<List<GetMatriculaGratuitaDTO>> listarTodas() {
        List<GetMatriculaGratuitaDTO> results = service.listarTodas();
        return ResponseEntity.ok(results);
    }
}

