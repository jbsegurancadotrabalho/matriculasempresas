package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostPerguntaDto {
	private String enunciado;
	private UUID idAvaliacaoCurso;
	private List<PostRespostaDto> respostas; // incluir lista de respostas junto, se desejar
}
