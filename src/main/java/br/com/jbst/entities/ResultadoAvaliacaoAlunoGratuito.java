package br.com.jbst.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "resultado_avaliacao_gratuito")
@NamedEntityGraph(
    name = "ResultadoAvaliacaoAlunoGratuito.comRespostasSelecionadas",
    attributeNodes = {
        @NamedAttributeNode(value = "respostasSelecionadasGratuito", subgraph = "respostasSelecionadas.subgraph")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "respostasSelecionadas.subgraph",
            attributeNodes = {
                @NamedAttributeNode("pergunta"),
                @NamedAttributeNode("respostaEad")
            }
        )
    }
)
public class ResultadoAvaliacaoAlunoGratuito {

    @Id
    @Column(name = "id_resultado")
    private UUID idResultado;

    @ManyToOne
    @JoinColumn(name = "id_avaliacao", nullable = false)
    private AvaliacaoCurso avaliacao;

    @Column(name = "nota", nullable = false)
    private double nota;

    @Column(name = "aprovado", nullable = false)
    private boolean aprovado;

    @Column(name = "data_resposta", nullable = false)
    private Instant dataResposta;


  
    @OneToMany(mappedBy = "resultadoAvaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespostaSelecionadaGratuita> respostasSelecionadasGratuito;

    
    @ManyToOne
    @JoinColumn(name = "id_matricula_gratuita")
    private MatriculasGratuita matriculasGratuita;


}
