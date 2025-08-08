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

import br.com.jbst.DtoEAD.GetModuloDto;
import br.com.jbst.DtoEAD.PostModuloDto;
import br.com.jbst.DtoEAD.PutModuloDto;
import br.com.jbst.services.ModuloService;

@RestController
@RequestMapping("/modulos")
public class ModuloControllers {

    @Autowired ModuloService moduloService;


    @PostMapping
    public ResponseEntity<GetModuloDto> criarModulo(@RequestBody PostModuloDto dto) {
        GetModuloDto novoModulo = moduloService.criarModulo(dto);
        return ResponseEntity.ok(novoModulo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetModuloDto> atualizarModulo(
            @PathVariable UUID id,
    		@RequestBody PutModuloDto dto) {
        GetModuloDto atualizado = moduloService.atualizarModulo(dto, id);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetModuloDto> buscarPorId(@PathVariable UUID id) {
        GetModuloDto modulo = moduloService.buscarPorId(id);
        return ResponseEntity.ok(modulo);
    }

    @GetMapping
    public ResponseEntity<List<GetModuloDto>> listarTodos() {
        List<GetModuloDto> lista = moduloService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        moduloService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
