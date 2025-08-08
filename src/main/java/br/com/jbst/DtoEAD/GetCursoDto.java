package br.com.jbst.DtoEAD;


import java.util.List;
import java.util.UUID;

import br.com.jbst.entities.AvaliacaoCurso;
import lombok.Data;

@Data
public class GetCursoDto {
    private UUID idcurso;
    private String curso;
    private Integer codigo;		
    private String descricao;
    private String conteudo;
    private List<GetModuloDto> modulo;	
    private List<GetAvaliacaoCursoDto> avaliacaoCurso;	


}
