package br.com.jbst.DTO1;

import java.util.UUID;

import lombok.Data;

@Data
public class GetCursoCertificadoDTO {
    private UUID idcurso;
	private Integer codigo;
	private String curso;
	private String modelo_certificado;
}
