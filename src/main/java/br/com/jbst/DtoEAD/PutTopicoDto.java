package br.com.jbst.DtoEAD;

import lombok.Data;

import java.util.UUID;

@Data
public class PutTopicoDto {
    private String titulo;
    private String conteudo;
    private Integer ordem;
}
