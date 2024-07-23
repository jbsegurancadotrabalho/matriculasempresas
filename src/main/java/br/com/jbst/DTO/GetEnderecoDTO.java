package br.com.jbst.DTO;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class GetEnderecoDTO {
    private UUID idEndereco;
    private String cep;
    private String logradouro;
    private String complemento;
    private String numero;
    private String bairro;
    private String localidade;
    private String uf;
}
