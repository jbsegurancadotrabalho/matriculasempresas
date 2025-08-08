package br.com.jbst.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "resposta_ead")
public class RespostaEad {

	@Id
	@Column(name = "idresposta")
	private UUID idResposta;

	@Column(name = "descricao", length = 1000, nullable = false)
	private String descricao;

	@Column(name = "correta", nullable = false)
	private boolean correta;

	@ManyToOne
	@JoinColumn(name = "id_perguntas", nullable = false)
	private Pergunta pergunta;
	
	
}
