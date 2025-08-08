package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class PostProgressoAlunoTopicoGratuitoDto {
	 private UUID idMatriculaGratuita; 
	    private UUID idTopico;
	    private boolean concluido;
}
