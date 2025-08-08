package br.com.jbst.DTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class PostMatriculaGratuitaDTO {
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