package br.com.jbst.DTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class GetFuncaoDTO {

	
	private UUID idFuncao;
	private String funcao;
	private String cbo;
    private String descricao;
}
