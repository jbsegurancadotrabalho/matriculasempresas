package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.UUID;

@Data
public class GetRespostaDto {
    private UUID idResposta;
    private String descricao;
    private boolean correta;
    private boolean selecionada;
    public boolean isSelecionada() {
        return selecionada;
    }
}
