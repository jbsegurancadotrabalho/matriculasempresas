package br.com.jbst.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.jbst.DTO.GetProficienciaDTO;
import br.com.jbst.services.ProficienciaService;

@RestController
@RequestMapping("/proficiencia")
public class ProficienciaController {

    @Autowired
    private ProficienciaService proficienciaService;

    @GetMapping("/todas")
    public ResponseEntity<List<GetProficienciaDTO>> listarTodas() {
        try {
            List<GetProficienciaDTO> lista = proficienciaService.listarTodas();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
