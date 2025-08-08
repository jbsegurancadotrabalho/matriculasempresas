package br.com.jbst.entities;


import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "progresso_aluno_topico_gratuito")
public class ProgressoAlunoTopicoGratuito {

	 @Id
	    @Column(name = "id_progresso")
	    private UUID idProgresso;

	    @ManyToOne
	    @JoinColumn(name = "id_topico", nullable = false)
	    private Topico topico;

	    @Column(name = "concluido", nullable = false)
	    private boolean concluido;

	    @Column(name = "data_conclusao")
	    private Instant dataConclusao;
	
	   
	    @ManyToOne
	    @JoinColumn(name = "id_matriculagratuita", nullable = false)
	    private MatriculasGratuita matriculasGratuita; 



	}
