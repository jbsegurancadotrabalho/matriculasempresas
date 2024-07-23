package br.com.jbst.DTO;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class GetUnidadeDeTreinamentoDTO {

	private UUID idUnidadedetreinamento;
	private String unidadedetreinamento;
	private String cnpj;
    private String unidade;
    private GetEnderecoDTO endereco;

}
