package br.com.jbst.entities;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "resposta_selecionada_gratuita")
public class RespostaSelecionadaGratuita {

	    
	    @Id
	    @GeneratedValue(generator = "UUID")
	    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "id_resposta_selecionada_gratuita", updatable = false, nullable = false)
	    private UUID idRespostaSelecionada;

	    
	    @ManyToOne
	    @JoinColumn(name = "id_pergunta", nullable = false)
	    private Pergunta pergunta;

	    @ManyToOne
	    @JoinColumn(name = "id_resposta", nullable = false)
	    private RespostaEad respostaEad;
	    
	    @ManyToOne
	    @JoinColumn(name = "idavaliacao") // nome da FK no banco
	    private AvaliacaoCurso avaliacao;
	    
	    @ManyToOne
	    @JoinColumn(name = "id_resultado") // ou o nome correto da coluna de FK
	    private ResultadoAvaliacaoAlunoGratuito resultadoAvaliacao;



	}



