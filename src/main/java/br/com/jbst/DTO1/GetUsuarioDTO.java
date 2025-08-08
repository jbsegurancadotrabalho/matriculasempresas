package br.com.jbst.DTO1;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class GetUsuarioDTO {
	private UUID id;
	private String nome;
	private String email;
	private List<GetFuncionarioCertificadoDTO> funcionarios;
}
