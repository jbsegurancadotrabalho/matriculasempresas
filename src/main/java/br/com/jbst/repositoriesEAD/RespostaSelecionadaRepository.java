package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.RespostaSelecionada;

public interface RespostaSelecionadaRepository extends JpaRepository<RespostaSelecionada, UUID>{

	List<RespostaSelecionada> findByResultado_IdResultado(UUID idResultado);

	 @Query("SELECT rs FROM RespostaSelecionada rs " +
	           "LEFT JOIN FETCH rs.pergunta " +
	           "LEFT JOIN FETCH rs.respostaEad " +
	           "WHERE rs.resultado.idResultado = :idResultado")
	    List<RespostaSelecionada> findByResultadoIdWithPerguntaAndResposta(@Param("idResultado") UUID idResultado);


}
