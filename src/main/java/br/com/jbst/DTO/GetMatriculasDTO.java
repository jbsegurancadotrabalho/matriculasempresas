package br.com.jbst.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetMatriculasDTO {
	
	private UUID idMatricula;
    private Integer numeroMatricula;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataHoraCriacao;
	private String venda;
	private BigDecimal valor;
	private String status;
	private String tipo_de_pagamento;
	private BigDecimal total;
	private List <CriarContaResponseDto> usuarios;
	private GetFuncionarioDTO funcionario;
	private GetTurmasDTO turmas;
	private List <GetEvidenciasDTO> evidencias;
}
