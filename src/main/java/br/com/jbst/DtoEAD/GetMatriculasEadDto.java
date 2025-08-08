package br.com.jbst.DtoEAD;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTO.GetFuncionarioDTO;
import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetMatriculasEadDto {
	private UUID idMatricula;
    private Integer numeroMatricula;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataHoraCriacao;
	private GetTurmasEadDto turmas;
	private GetFuncionarioDTO funcionario;
}
