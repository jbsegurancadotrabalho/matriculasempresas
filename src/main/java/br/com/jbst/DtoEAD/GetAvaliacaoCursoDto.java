package br.com.jbst.DtoEAD;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class GetAvaliacaoCursoDto {
	private UUID idAvaliacaoCurso;
	private String nomeAvaliacao;
	private String descricaoAvaliacao;
	private UUID idcurso;
	private String nomeCurso;
	private List<GetPerguntaDto> perguntas;
}
