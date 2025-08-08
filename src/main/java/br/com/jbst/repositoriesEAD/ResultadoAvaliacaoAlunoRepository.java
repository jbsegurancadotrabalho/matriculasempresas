package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.ResultadoAvaliacaoAluno;

public interface ResultadoAvaliacaoAlunoRepository extends JpaRepository<ResultadoAvaliacaoAluno, UUID> {

    List<ResultadoAvaliacaoAluno> findByMatricula_IdMatricula(UUID idMatricula);



    @Query("""
    	    SELECT DISTINCT r FROM ResultadoAvaliacaoAluno r
    	    LEFT JOIN FETCH r.respostasSelecionadas rs
    	    LEFT JOIN FETCH rs.pergunta
    	    LEFT JOIN FETCH rs.respostaEad
    	    WHERE r.matricula.idMatricula = :idMatricula
    	""")
    	List<ResultadoAvaliacaoAluno> findByMatriculaIdMatriculaComRespostas(@Param("idMatricula") UUID idMatricula);
}