package br.com.jbst.DTO;


import java.util.UUID;
import lombok.Data;

@Data
public class GetCursoDTO {
    private UUID idcurso;
	private Integer codigo;
	private String curso;
	private String modelo_certificado;

}
