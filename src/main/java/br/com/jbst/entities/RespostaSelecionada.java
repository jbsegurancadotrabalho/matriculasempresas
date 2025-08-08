package br.com.jbst.entities;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "resposta_selecionada")
public class RespostaSelecionada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    @JoinColumn(name = "id_resultado")
    private ResultadoAvaliacaoAluno resultado;
    
 
}

