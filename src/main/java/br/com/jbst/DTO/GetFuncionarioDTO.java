package br.com.jbst.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class GetFuncionarioDTO {

	private UUID idFuncionario;
	private String nome;
	private String cpf;
	private String rg;
	private GetEmpresaDTO empresa;
	private GetFuncaoDTO funcao;

}
