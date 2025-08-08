package br.com.jbst.DtoEAD;

import lombok.Data;

@Data
public class ResumoProgressoCursoGratuitoDto {
	private int totalTopicos;
	private int topicosConcluidos;
	private double percentualConcluido;
}
