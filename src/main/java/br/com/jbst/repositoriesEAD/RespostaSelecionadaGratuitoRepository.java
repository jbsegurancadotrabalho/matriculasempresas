package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.RespostaSelecionadaGratuita;

public interface RespostaSelecionadaGratuitoRepository extends JpaRepository<RespostaSelecionadaGratuita, UUID> {

    // Corrigido: nome correto do atributo é resultado_gratuito
    List<RespostaSelecionadaGratuita> findByResultadoAvaliacao_IdResultado(UUID idResultado);

    @Query("SELECT rs FROM RespostaSelecionadaGratuita rs LEFT JOIN FETCH rs.pergunta LEFT JOIN FETCH rs.respostaEad WHERE rs.resultadoAvaliacao.idResultado = :idResultado")
    List<RespostaSelecionadaGratuita> findByResultadoIdWithPerguntaAndResposta(UUID idResultado);

}
