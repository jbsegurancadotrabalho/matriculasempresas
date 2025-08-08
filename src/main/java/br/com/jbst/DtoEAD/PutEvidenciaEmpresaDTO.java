package br.com.jbst.DtoEAD;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PutEvidenciaEmpresaDTO {
	private UUID idEvidencias;
	private Instant dataHoraCriacao;
	private String nome;
	private String descricao;
	private byte[] inserir_evidencias;

}
