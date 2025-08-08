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

import br.com.jbst.DtoEAD.GetPerguntaTopicoDto;
import br.com.jbst.DtoEAD.PostPerguntaTopicoDto;
import br.com.jbst.DtoEAD.PutPerguntaTopicoDto;
import br.com.jbst.services.PerguntaTopicoService;

@RestController
@RequestMapping("/api/perguntas")
public class PerguntaTopicoController {

	
	@Autowired PerguntaTopicoService perguntaService;

    // ✅ Criar nova pergunta vinculada a um tópico
    @PostMapping
    public ResponseEntity<GetPerguntaTopicoDto> criarPergunta(@RequestBody PostPerguntaTopicoDto dto) {
        GetPerguntaTopicoDto criada = perguntaService.criarPergunta(dto);
        return ResponseEntity.ok(criada);
    }

    // ✅ Atualizar pergunta existente
    @PutMapping("/{id}")
    public ResponseEntity<GetPerguntaTopicoDto> atualizarPergunta(@PathVariable UUID id, @RequestBody PutPerguntaTopicoDto dto) {
        dto.setIdPergunta(id);
        GetPerguntaTopicoDto atualizada = perguntaService.atualizarPergunta(dto);
        return ResponseEntity.ok(atualizada);
    }

    // ✅ Listar perguntas por tópico
    @GetMapping("/topico/{idTopico}")
    public ResponseEntity<List<GetPerguntaTopicoDto>> listarPorTopico(@PathVariable UUID idTopico) {
        List<GetPerguntaTopicoDto> lista = perguntaService.listarPorTopico(idTopico);
        return ResponseEntity.ok(lista);
    }

    // ✅ Buscar pergunta por ID
    @GetMapping("/{id}")
    public ResponseEntity<GetPerguntaTopicoDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(perguntaService.buscarPorId(id));
    }

    // ✅ Deletar pergunta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        perguntaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
