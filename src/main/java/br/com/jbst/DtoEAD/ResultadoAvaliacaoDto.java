package br.com.jbst.DtoEAD;


import java.time.Instant;

import lombok.Data;

@Data
public class ResultadoAvaliacaoDto {
    private double nota;
    private boolean aprovado;
    private Instant dataResposta;
}

