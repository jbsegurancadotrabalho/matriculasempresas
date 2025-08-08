package br.com.jbst.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DtoEAD.GetEvidenciaEmpresaDTO;
import br.com.jbst.DtoEAD.PostEvidenciaEmpresaDTO;
import br.com.jbst.components.MatriculasMessageProducer;
import br.com.jbst.services.HistoricoEvidenciasService;

@RestController
@RequestMapping("/api/evidencias")
public class HistoricoEvidenciaController {

    @Autowired
    private HistoricoEvidenciasService historicoEvidenciasService;

    @Autowired
    private MatriculasMessageProducer producer;

    @PostMapping(value = "/upload-test", consumes = {"multipart/form-data"})
    public ResponseEntity<GetEvidenciaEmpresaDTO> criarEvidencia(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("idEmpresa") UUID idEmpresa,
            @RequestParam("idTurmas") UUID idTurmas,
            @RequestParam("arquivo") MultipartFile arquivo) {

        try {
            // Prepara DTO da evidência
            PostEvidenciaEmpresaDTO dto = new PostEvidenciaEmpresaDTO();
            dto.setNome(nome);
            dto.setDescricao(descricao);
            dto.setIdEmpresa(idEmpresa);
            dto.setIdTurmas(idTurmas);

            byte[] arquivoBytes = arquivo.getBytes();

            // Cria evidência
            GetEvidenciaEmpresaDTO evidenciaCriada = historicoEvidenciasService.criarEvidencia(dto, arquivoBytes);

            // Prepara mensagem de e-mail com anexo
            MailSenderDto mensagem = new MailSenderDto();
            mensagem.setMailTo("operacional@jbsegurancadotrabalho.com.br"); // ou dinâmico
            mensagem.setSubject("Nova Evidência Criada");
            mensagem.setBody(
                "<strong>Nome:</strong> " + nome + "<br>" +
                "<strong>Descrição:</strong> " + descricao
            );
            mensagem.setAttachment(arquivoBytes);
            mensagem.setAttachmentName("evidencia.pdf");

            // Envia para a fila RabbitMQ
            producer.sendMessage(mensagem);

            return ResponseEntity.ok(evidenciaCriada);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
