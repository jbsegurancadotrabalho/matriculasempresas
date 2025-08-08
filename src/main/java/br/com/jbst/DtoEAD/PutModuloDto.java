package br.com.jbst.DtoEAD;

import java.util.UUID;

import lombok.Data;

@Data
public class PutModuloDto {
	private UUID idModulo;
    private String titulo;
    private String descricao;
}
