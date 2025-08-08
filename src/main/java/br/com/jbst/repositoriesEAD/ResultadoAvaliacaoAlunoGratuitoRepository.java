package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.ResultadoAvaliacaoAlunoGratuito;

public interface ResultadoAvaliacaoAlunoGratuitoRepository extends JpaRepository<ResultadoAvaliacaoAlunoGratuito, UUID> {


    List<ResultadoAvaliacaoAlunoGratuito> findByMatriculasGratuita_IdMatriculaGratuita(UUID idMatriculaGratuito);


    @Query("""
    	    SELECT DISTINCT r FROM ResultadoAvaliacaoAlunoGratuito r
    	    LEFT JOIN FETCH r.respostasSelecionadasGratuito rs
    	    LEFT JOIN FETCH rs.pergunta
    	    LEFT JOIN FETCH rs.respostaEad
    	    WHERE r.matriculasGratuita.idMatriculaGratuita = :idMatriculaGratuita
    	""")
    	List<ResultadoAvaliacaoAlunoGratuito> findByMatriculaIdMatriculaGratuitaComRespostas(@Param("idMatriculaGratuita") UUID idMatriculaGratuita);


}