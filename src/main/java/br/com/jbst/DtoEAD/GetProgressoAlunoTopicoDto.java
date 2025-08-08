package br.com.jbst.DtoEAD;

	
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class GetProgressoAlunoTopicoDto {
    private UUID idProgresso;
    private UUID idMatricula;
    private UUID idTopico;
    private String tituloTopico;
    private boolean concluido;
    private Instant dataConclusao;
	
}
