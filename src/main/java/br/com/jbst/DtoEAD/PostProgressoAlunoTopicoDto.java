package br.com.jbst.DtoEAD;

	
import lombok.Data;

import java.util.UUID;

import lombok.Data;
import java.util.UUID;

@Data
public class PostProgressoAlunoTopicoDto {
    private UUID idMatricula; 
    private UUID idTopico;
    private boolean concluido;
	
}
