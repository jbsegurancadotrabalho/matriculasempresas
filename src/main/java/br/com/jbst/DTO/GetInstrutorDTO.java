package br.com.jbst.DTO;

import java.util.List;
import java.util.UUID;

import lombok.Data;


@Data
public class GetInstrutorDTO {
	private UUID idinstrutor;
	private String instrutor;
	private String rg;
	private String cpf;
	private List<GetProficienciaDTO> proficiencias;
	private List<GetFormacaoDTO> formacoes;



}
