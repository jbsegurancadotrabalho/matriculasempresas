package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class PostRespostaDto {
	private UUID idPergunta;
	private String descricao;
	private boolean correta;
}
