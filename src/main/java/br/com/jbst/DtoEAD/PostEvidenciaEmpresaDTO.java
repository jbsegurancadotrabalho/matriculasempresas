package br.com.jbst.DtoEAD;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PostEvidenciaEmpresaDTO {
	private Instant dataHoraCriacao;
	private String nome;
	private String descricao;
	private byte[] inserir_evidencias;
	private UUID idTurmas;
	private UUID idEmpresa;

}