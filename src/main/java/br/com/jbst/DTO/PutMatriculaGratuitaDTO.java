package br.com.jbst.DTO;


import java.time.Instant;
import java.util.UUID;

import lombok.Data;


@Data
public class PutMatriculaGratuitaDTO {
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
    private UUID idTurmas;
}
