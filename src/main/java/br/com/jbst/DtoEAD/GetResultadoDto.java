package br.com.jbst.DtoEAD;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.jbst.DTO.GetMatriculasDTO;
import br.com.jbst.config.InstantSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetResultadoDto {
	private UUID idResultado;
	private String nomeAvaliacao;
	private double nota;
	private boolean aprovado;
	@JsonSerialize(using = InstantSerializer.class)
	private Instant dataResposta;
	private GetAvaliacaoCursoDto avaliacao;
	private GetMatriculasDTO matricula;
    private boolean correta; // nova propriedade indicando se a resposta marcada estava correta
    private List <GetRespostaSelecionadaDto> respostasSelecionadas;
}
