package br.com.jbst.entities;


import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "avaliacao_curso")
public class AvaliacaoCurso {

    @Id
    @Column(name = "idavaliacao")
    private UUID idAvaliacaoCurso;
    
	// Campo 3
	@Column(name = "nome_avaliacao", length = 500, nullable = false)
	private String nomeAvaliacao;
	
	// Campo 3
	@Column(name = "descricao_avaliacao", length = 500, nullable = false)
	private String descricaoAvaliacao;

    @ManyToOne
    @JoinColumn(name = "idcurso", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pergunta> perguntas;
    
    
    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultadoAvaliacaoAluno> avaliacao;
    
    
    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespostaSelecionada> respostaSelecionada;
    
    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespostaSelecionadaGratuita> respostaSelecionadaGratuita;
    
    
}
