package br.com.jbst.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "matricula_gratuita")
public class MatriculasGratuita {

	// Campo 1
	@Id
	@Column(name = "idmatricula")
	private UUID idMatriculaGratuita;

	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false)
	private Instant dataHoraCriacao;

	// Campo 5
	@Column(name = "numeromatricula_gratuita", nullable = true)
	private Integer numeroMatriculaGratuita;

	// Campo 3
	@Column(name = "razaosocial", length = 100, nullable = false)
	private String razaosocial;

	// Campo 5
	@Column(name = "cnpj", length = 100, nullable = false)
	private String cnpj;

	@Column(name = "funcionario", length = 100, nullable = false)
	private String funcionario;

	// Campo 4
	@Column(name = "cpf", length = 100, nullable = true)
	private String cpf;

	// Campo 5
	@Column(name = "rg", length = 100, nullable = false)
	private String rg;

	@Column(name = "funcao", length = 100, nullable = false)
	private String funcao;

	// Campo 10
	@Column(name = "responsavel", length = 100, nullable = true)
	private String responsavel;

	// Campo 11
	@Column(name = "email", length = 100, nullable = true)
	private String email;
	
	// Campo 13
	@Column(name = "whatsapp", length = 100, nullable = true)
	private String whatsapp;

	// Campo 9
	@ManyToOne
	@JoinColumn(name = "id_turmas", nullable = true)
	private Turmas turmas;



	
	@OneToMany(mappedBy = "matriculasGratuita", cascade = CascadeType.ALL)
	private List<ProgressoAlunoTopicoGratuito> progressoAlunoTopico;

	
	
	// Na classe MatriculasGratuita:
	@OneToMany(mappedBy = "matriculasGratuita", cascade = CascadeType.ALL)
	private List<ResultadoAvaliacaoAlunoGratuito> resultados;



}
