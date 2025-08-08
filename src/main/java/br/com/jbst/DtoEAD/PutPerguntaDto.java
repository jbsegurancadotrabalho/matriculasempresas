package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PutPerguntaDto {
    private String enunciado;
    private UUID idAvaliacao;
    private List<PostRespostaDto> respostas;
}
	