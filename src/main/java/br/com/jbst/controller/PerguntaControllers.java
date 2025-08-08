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
import br.com.jbst.DtoEAD.GetPerguntaDto;
import br.com.jbst.DtoEAD.PostPerguntaDto;
import br.com.jbst.DtoEAD.PutPerguntaDto;
import br.com.jbst.services.PerguntaService;

@RestController
@RequestMapping("/perguntas")
public class PerguntaControllers {

    @Autowired PerguntaService perguntaService;


    @PostMapping
    public ResponseEntity<GetPerguntaDto> criarPergunta(@RequestBody PostPerguntaDto dto) {
        return ResponseEntity.ok(perguntaService.criarPergunta(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPerguntaDto> atualizarPergunta(
            @PathVariable UUID id,
            @RequestBody PutPerguntaDto dto
    ) {
        return ResponseEntity.ok(perguntaService.atualizarPergunta(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPerguntaDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(perguntaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<GetPerguntaDto>> listarTodas() {
        return ResponseEntity.ok(perguntaService.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        perguntaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
