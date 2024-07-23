package br.com.jbst.entities;
import java.time.Instant;

import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "turmas")
public class Turmas {
	
	//Campo 1
	@Id
	@Column(name = "idturmas")
	private UUID idTurmas;
	
	//Campo 2
	@Column(name = "numeroturma", nullable = false)
	private Integer numeroTurma;
	
	//Campo 3
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datainicio", nullable = false)
	private Instant datainicio;
	
	//Campo 4
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datafim", nullable = false)
	private Instant datafim;
	
	//Campo 5
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validadedocurso", nullable = true)
	private Instant validadedocurso;
	
	//Campo 6
	@Column(name = "cargahoraria", nullable = false)
	private String  cargahoraria;
	
	//Campo 7
	@Column(name = "modalidade", nullable = false)
	private String modalidade;
	
	//Campo 8
	@Column(name = "status", nullable = false)
	private String status;
	
	//Campo 9
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	//Campo 10
	@Column(name = "diasespecificos", nullable = false)
	private String diasespecificos;
	
	//Campo 11
	@Column(name = "tipo", nullable = false)
	private String tipo;
	
	//Campo 12
	@Column(name = "nivel", nullable = false)
	private String nivel;
	
	//Campo 13
	@Column(name = "validade", nullable = false)
	private String validade;
	
	//Campo 14
	@Column(name = "dia", nullable = false)
	private String dia;
	
	//Campo 15
	@Column(name = "mes", nullable = false)
	private String mes;
	
	//Campo 16
	@Column(name = "ano", nullable = false)
	private String ano;
	
	//Campo 17
	@Column(name = "primeirodia", nullable = false)
	private String primeirodia;

	//Campo 18
	@Column(name = "segundodia", nullable = true)
	private String segundodia;

	//Campo 19
	@Column(name = "terceirodia", nullable = true)
	private String terceirodia;

	//Campo 20
	@Column(name = "quartodia", nullable = true)
	private String quartodia;

	//Campo 21
	@Column(name = "quintodia", nullable = true)
	private String quintodia;
	
	//Campo 22
	@Column(name = "instrutor", nullable = true)
	private String instrutor;
	
	//Campo 23
	@Column(name = "turma_fechada", nullable = false)
	private boolean turmaFechada;
	
	//Campo 24
	@Column(name = "matriculas_bloqueadas", nullable = false)
	private boolean matriculasBloqueadas;
    
	//Campo 25
    @ManyToOne
    @JoinColumn(name = "idunidadedetreinamento", referencedColumnName = "idUnidadedetreinamento", nullable = true)
    private UnidadeDeTreinamento unidadeDeTreinamento;
	
	//Campo 26
	@ManyToOne // muitos contatos  para 1 empresa
	@JoinColumn(name = "idcurso", nullable = true) // O JoinColumn é para mapeamento de chave estrangeira//
	private Curso curso;
    
	@OneToMany(mappedBy = "turmas") //1 Empresa tem muitos Funcionários
	private List<Matriculas> matricula;
	

	  @ManyToMany
	    @JoinTable(
	        name = "turma_instrutor",
	        joinColumns = @JoinColumn(name = "idturmas"),
	        inverseJoinColumns = @JoinColumn(name = "idinstrutor", nullable = true )
	    )
	    private List<Instrutor> instrutores;
	
}
