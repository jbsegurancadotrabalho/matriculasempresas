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

import br.com.jbst.DtoEAD.GetTopicoDto;
import br.com.jbst.DtoEAD.PostTopicoDto;
import br.com.jbst.DtoEAD.PutTopicoDto;
import br.com.jbst.services.TopicoService;

@RestController
@RequestMapping("/topicos")
public class TopicoControllers {

    @Autowired TopicoService topicoService;

  

    @PostMapping
    public ResponseEntity<GetTopicoDto> criarTopico(@RequestBody PostTopicoDto dto) {
        GetTopicoDto novoTopico = topicoService.criarTopico(dto);
        return ResponseEntity.ok(novoTopico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetTopicoDto> atualizarTopico(@PathVariable UUID id, @RequestBody PutTopicoDto dto) {
        GetTopicoDto atualizado = topicoService.atualizarTopico(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping
    public ResponseEntity<List<GetTopicoDto>> listarTodos() {
        return ResponseEntity.ok(topicoService.listarTopicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTopicoDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(topicoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        topicoService.deletarTopico(id);
        return ResponseEntity.noContent().build();
    }
}
