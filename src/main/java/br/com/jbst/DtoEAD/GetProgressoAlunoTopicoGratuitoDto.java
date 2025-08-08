package br.com.jbst.DtoEAD;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;


@Data
public class GetProgressoAlunoTopicoGratuitoDto {
    private UUID idProgresso;
    private UUID idMatriculaGratuita;
    private UUID idTopico;
    private String tituloTopico;
    private boolean concluido;
    private Instant dataConclusao;
}
