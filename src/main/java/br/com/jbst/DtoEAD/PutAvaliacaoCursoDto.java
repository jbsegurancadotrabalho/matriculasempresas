package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.UUID;

@Data
public class PutAvaliacaoCursoDto {
    private UUID idAvaliacaoCurso;
    private String nomeAvaliacao;
	private String descricaoAvaliacao;
}
					