package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GetPerguntaDto {
    private UUID idPergunta;
    private String enunciado;
    private UUID idAvaliacaoCurso;
    private List<GetRespostaDto> respostas;
}
