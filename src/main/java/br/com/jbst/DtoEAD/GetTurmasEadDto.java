package br.com.jbst.DtoEAD;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTO.GetCursoDTO;
import br.com.jbst.DTO.GetInstrutorDTO;
import br.com.jbst.DTO.GetUnidadeDeTreinamentoDTO;
import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetTurmasEadDto {
	private UUID idTurmas;	
	private Integer numeroTurma;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant datainicio;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant datafim;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant validadedocurso;
	private String  cargahoraria;
	private String modalidade;
	private String status;
	private String nivel;
	private String tipo;
	private String descricao;
	private String diasespecificos;
	private String validade;
	private String dia;
	private String mes;
	private String ano;
	private String primeirodia;
	private String segundodia;
	private String terceirodia;
	private String quartodia;
	private String quintodia;
	private GetCursoDto curso;
	private List<GetInstrutorDTO> instrutores;
	private GetUnidadeDeTreinamentoDTO unidadeDeTreinamento;
}
