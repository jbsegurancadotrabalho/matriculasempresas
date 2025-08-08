package br.com.jbst.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "pergunta")
public class Pergunta {

	@Id
	@Column(name = "idpergunta")
	private UUID idPergunta;

	@Column(name = "enunciado", length = 1000, nullable = false)
	private String enunciado;

	@ManyToOne
	@JoinColumn(name = "idavaliacao", nullable = true)
	private AvaliacaoCurso avaliacao;

	@OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RespostaEad> respostas;

	@ManyToOne
	@JoinColumn(name = "idtopico", nullable = true)
	private Topico topico;

}
