package br.com.jbst.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.jbst.DtoEAD.GetEvidenciaEmpresaDTO;
import br.com.jbst.DtoEAD.PostEvidenciaEmpresaDTO;
import br.com.jbst.DtoEAD.PutEvidenciaEmpresaDTO;
import br.com.jbst.services.EvidenciaService;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    @Autowired
    private EvidenciaService evidenciaService;


    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<GetEvidenciaEmpresaDTO> criarEvidencia(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("idEmpresa") UUID idEmpresa,
            @RequestParam("idTurmas") UUID idTurmas,
            @RequestParam("arquivo") MultipartFile arquivo) {

        try {
            PostEvidenciaEmpresaDTO dto = new PostEvidenciaEmpresaDTO();
            dto.setNome(nome);
            dto.setDescricao(descricao);
            dto.setIdEmpresa(idEmpresa);
            dto.setIdTurmas(idTurmas);

            GetEvidenciaEmpresaDTO evidenciaCriada = evidenciaService.criarEvidencia(dto, arquivo.getBytes());
            return ResponseEntity.ok(evidenciaCriada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping
    public ResponseEntity<GetEvidenciaEmpresaDTO> editarEvidencia(@RequestBody PutEvidenciaEmpresaDTO dto) {
        GetEvidenciaEmpresaDTO evidenciaAtualizada = evidenciaService.editarEvidencia(dto);
        return ResponseEntity.ok(evidenciaAtualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetEvidenciaEmpresaDTO> consultarPorId(@PathVariable UUID id) {
        GetEvidenciaEmpresaDTO evidencia = evidenciaService.consultarPorId(id);
        return ResponseEntity.ok(evidencia);
    }

    @GetMapping
    public ResponseEntity<List<GetEvidenciaEmpresaDTO>> consultarTodas() {
        List<GetEvidenciaEmpresaDTO> evidencias = evidenciaService.consultarTodas();
        return ResponseEntity.ok(evidencias);
    }
}

