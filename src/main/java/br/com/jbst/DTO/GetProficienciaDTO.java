package br.com.jbst.DTO;


import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class GetProficienciaDTO {
    private UUID idProficiencia;
    private Instant dataHoraCriacao;
    private String proficiencia;
    private String descricao;
    private byte[] inserir_proficiencia;

    // Dados do Instrutor
    private UUID idInstrutor;
    private String nomeInstrutor;
    private String emailInstrutor;
}
