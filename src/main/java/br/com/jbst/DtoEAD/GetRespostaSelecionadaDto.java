package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class GetRespostaSelecionadaDto {
    private UUID idRespostaSelecionada;
    private UUID idPergunta;
    private String tituloPergunta;
    private UUID idResposta;
    private String descricaoResposta;
    private boolean respostaCorreta; // <-- novo campo
}



