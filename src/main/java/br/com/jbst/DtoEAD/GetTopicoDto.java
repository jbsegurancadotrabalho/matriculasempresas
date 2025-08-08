package br.com.jbst.DtoEAD;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class GetTopicoDto {
    private UUID idTopico;
    private String titulo;
    private String conteudo;
    private Integer ordem;
    private UUID idModulo;
    private List<GetPerguntaDto> perguntas;
}
