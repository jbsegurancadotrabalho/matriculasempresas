package br.com.jbst.DtoEAD;


import lombok.Data;

@Data
public class ResumoProgressoCursoDto {
    private int totalTopicos;
    private int topicosConcluidos;
    private double percentualConcluido;
}

