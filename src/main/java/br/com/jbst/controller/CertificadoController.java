package br.com.jbst.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.jbst.entities.Empresa;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.services.EnvioLinkCertificadosService;

@RestController
@RequestMapping("/api/teste-envio-certificados")
public class CertificadoController {

    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private TurmasRepository turmasRepository;
    @Autowired private EnvioLinkCertificadosService envioService;

    @GetMapping
    public ResponseEntity<String> enviarManual(@RequestParam UUID idEmpresa, @RequestParam UUID idTurma) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        Turmas turma = turmasRepository.findById(idTurma)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        envioService.enviarLinksPorEmail(empresa, turma);
        return ResponseEntity.ok("Enviado com sucesso!");
    }
}

