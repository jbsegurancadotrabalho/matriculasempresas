package br.com.jbst.DtoEAD;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RespostaAlunoDto {
    private UUID idAvaliacaoCurso;
    private List<UUID> respostasSelecionadas; // Lista de IDs de respostas marcadas
}
