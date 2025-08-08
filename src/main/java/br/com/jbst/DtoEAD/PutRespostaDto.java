package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.UUID;

@Data
public class PutRespostaDto {
    private UUID idResposta;
    private String descricao;
    private boolean correta;
}
