package br.com.jbst.DTO;

import java.time.Instant;

import lombok.Data;

@Data
public class ResultadoAvaliacaoGratuitaDto {
    private double nota;
    private boolean aprovado;
    private Instant dataResposta;
}
