package br.com.jbst.DtoEAD;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class GetModuloDto {
	private UUID idModulo;
	private String titulo;
	private String descricao;
	private List<GetTopicoDto> topicos;
}
