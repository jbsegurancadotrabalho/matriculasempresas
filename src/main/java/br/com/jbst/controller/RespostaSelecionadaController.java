package br.com.jbst.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jbst.DtoEAD.GetRespostaSelecionadaDto;
import br.com.jbst.services.RespostaSelecionadaService;

@RestController
@RequestMapping("/respostas-selecionadas")
public class RespostaSelecionadaController {

    @Autowired
    RespostaSelecionadaService respostaSelecionadaService;

    @GetMapping("/todas")
    public List<GetRespostaSelecionadaDto> buscarTodasUltimasRespostas() {
        return respostaSelecionadaService.buscarUltimasRespostasSelecionadas();
    }
}
