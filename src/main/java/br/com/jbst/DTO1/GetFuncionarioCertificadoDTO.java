package br.com.jbst.DTO1;

import java.util.List;
import java.util.UUID;

import br.com.jbst.DTO.GetMatriculasDTO;
import lombok.Data;

@Data
public class GetFuncionarioCertificadoDTO {
	private UUID idFuncionario;
	private String nome;
	private String cpf;
	private String rg;
	private List<GetMatriculasCertificadoDTO> matriculas;

}