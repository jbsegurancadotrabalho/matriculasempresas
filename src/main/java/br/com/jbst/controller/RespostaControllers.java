package br.com.jbst.controller;

import java.util.List;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DtoEAD.GetRespostaDto;
import br.com.jbst.DtoEAD.PostRespostaDto;
import br.com.jbst.DtoEAD.PutRespostaDto;
import br.com.jbst.entities.RespostaEad;
import br.com.jbst.services.RespostaService;

@RestController
@RequestMapping("/respostas")
public class RespostaControllers {

	@Autowired RespostaService respostaService;

	

	@PostMapping
    public ResponseEntity<GetRespostaDto> criarResposta(@RequestBody PostRespostaDto dto) {
        GetRespostaDto respostaCriada = respostaService.criarResposta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaCriada);
    }

	// Atualizar resposta
	@PutMapping("/{id}")
	public ResponseEntity<GetRespostaDto> atualizarResposta(@PathVariable UUID id, @RequestBody PutRespostaDto dto) {
		return ResponseEntity.ok(respostaService.atualizarResposta(id, dto));
	}

	// Buscar resposta por ID
	@GetMapping("/{id}")
	public ResponseEntity<GetRespostaDto> buscarPorId(@PathVariable UUID id) {
		return ResponseEntity.ok(respostaService.buscarPorId(id));
	}

	// Listar todas as respostas
	@GetMapping
	public ResponseEntity<List<GetRespostaDto>> listarTodas() {
		return ResponseEntity.ok(respostaService.listarTodas());
	}

	// Deletar resposta
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable UUID id) {
		respostaService.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	 @GetMapping("/pergunta/{idPergunta}")
	    public ResponseEntity<List<GetRespostaDto>> listarPorPergunta(@PathVariable UUID idPergunta) {
	        List<GetRespostaDto> respostas = respostaService.listarPorPergunta(idPergunta);
	        return ResponseEntity.ok(respostas);
	    }
}
