package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class PostPerguntaTopicoDto {
	private String enunciado;
    private UUID idTopico;
}
