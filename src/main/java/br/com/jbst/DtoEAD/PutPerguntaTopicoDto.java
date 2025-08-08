package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class PutPerguntaTopicoDto {
    private UUID idPergunta;
	private String enunciado;
 
}
