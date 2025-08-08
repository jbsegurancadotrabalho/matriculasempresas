package br.com.jbst.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DtoEAD.GetCursoDto;
import br.com.jbst.services.CursoServices;

@RestController
@RequestMapping("/api/curso")
public class CursoController {

    @Autowired
    private CursoServices cursoService;

    @GetMapping("/{idcurso}")
    public ResponseEntity<GetCursoDto> buscarCursoPorId(@PathVariable UUID idcurso) {
        GetCursoDto cursoDto = cursoService.buscarCursoPorId(idcurso);
        return ResponseEntity.ok(cursoDto);
    }
}
