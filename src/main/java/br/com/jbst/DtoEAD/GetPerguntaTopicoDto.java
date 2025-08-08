package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class GetPerguntaTopicoDto {
	 private UUID idPergunta;
		private String enunciado;
}
