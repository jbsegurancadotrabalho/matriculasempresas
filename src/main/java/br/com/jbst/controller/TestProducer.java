package br.com.jbst.controller;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbst.DTO.MailSenderDto;
import lombok.Value;

@RestController
@RequestMapping("/test")
public class TestProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;
    
  

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MailSenderDto dto) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);
        amqpTemplate.convertAndSend("mensagens_usuarios", json); // use o nome literal
        return ResponseEntity.ok("Mensagem enviada com sucesso");
    }
}

