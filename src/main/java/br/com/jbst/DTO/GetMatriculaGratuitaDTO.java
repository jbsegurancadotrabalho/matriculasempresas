package br.com.jbst.DTO;



import java.time.Instant;
import java.util.UUID;

import br.com.jbst.DtoEAD.GetTurmasEadDto;
import lombok.Data;

@Data
public class GetMatriculaGratuitaDTO {
    private UUID idMatriculaGratuita;
    private Instant dataHoraCriacao;
    private Integer numeroMatriculaGratuita;
    private String razaosocial;
    private String cnpj;
    private String funcionario;
    private String cpf;
    private String rg;
    private String funcao;
    private String responsavel;
    private String email;
    private String whatsapp;
	private GetTurmasEadDto turmas;
}
