package br.com.jbst.DTO1;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.config.InstantSerializer;
import lombok.Data;

@Data
public class GetMatriculasCertificadoDTO {
	private UUID idMatricula;
    private Integer numeroMatricula;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataHoraCriacao;
	private String venda;
	private BigDecimal valor;
	private String status;
	private String tipo_de_pagamento;
	private BigDecimal total;
	private GetTurmasCertificadoDTO turmas;

}
