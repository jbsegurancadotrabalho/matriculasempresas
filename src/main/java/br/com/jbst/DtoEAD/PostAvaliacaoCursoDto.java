package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Column;

@Data
public class PostAvaliacaoCursoDto {
	private UUID idcurso;
	private String nomeAvaliacao;
	private String descricaoAvaliacao;
}
