package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class PostModuloDto {
	private String titulo;
	private String descricao;
	private UUID idcurso; // curso ao qual o módulo pertences
}
