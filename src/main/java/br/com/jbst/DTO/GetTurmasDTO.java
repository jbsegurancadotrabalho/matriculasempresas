package br.com.jbst.DTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.config.InstantSerializer;
import lombok.Data;


@Data
public class GetTurmasDTO {

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
	private GetCursoDTO curso;
	private GetUnidadeDeTreinamentoDTO unidadeDeTreinamento;
	private List<GetInstrutorDTO> instrutores;

}
