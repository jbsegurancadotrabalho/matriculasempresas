package br.com.jbst.controller;


import java.util.List;


import br.com.jbst.DTO.GetFormacaoDTO;
import br.com.jbst.services.FormacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/formacoes")
public class FormacaoController {

    @Autowired
    private FormacaoService formacaoService;

    @GetMapping("/consultar")
    public List<GetFormacaoDTO> consultarFormacoes() {
        return formacaoService.consultarFormacao();
    }
}
