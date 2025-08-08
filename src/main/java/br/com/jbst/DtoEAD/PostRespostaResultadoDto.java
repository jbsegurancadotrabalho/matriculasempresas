package br.com.jbst.DtoEAD;


import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para envio de respostas de uma avaliação por um aluno.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRespostaResultadoDto {
    private UUID idAvaliacaoCurso;
    private List<RespostaDto> respostas;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RespostaDto {      
        private UUID idPergunta;
        private UUID idResposta;
    }
}
