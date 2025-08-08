package br.com.jbst.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import br.com.jbst.DtoEAD.GetProgressoAlunoTopicoDto;

@Data
@Entity
@Table(name = "topico")
public class Topico {

    @Id
    @Column(name = "idtopico")
    private UUID idTopico;

    @Column(name = "titulo", length = 200, nullable = false)
    private String titulo;

    @Column(name = "conteudo", columnDefinition = "TEXT", nullable = true)
    private String conteudo;

    @Column(name = "ordem", nullable = true)
    private Integer ordem;

    @ManyToOne
    @JoinColumn(name = "idmodulo", nullable = false)
    private Modulo modulo;
    
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pergunta> perguntas;

	public Topico getTopico() {
		return null;
	}

	public GetProgressoAlunoTopicoDto getMatriculas() {
		return null;
	}


}
