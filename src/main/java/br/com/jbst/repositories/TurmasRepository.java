package br.com.jbst.repositories;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Turmas;

public interface TurmasRepository extends JpaRepository<Turmas, UUID >{

	@Query(
			"select t from Turmas t "

	)
	List<Turmas> findAllByDescricao();
	
	
	@Query(
			"select t from Turmas t "
		  + "left join t.instrutores i "
		  + "where t.id = :id "
	)
	Turmas find(@Param("id") UUID id);
	
	
	@Query("select t from Turmas t where t.id = :id ")
	Turmas findIdTurmas(@Param("id") UUID id);
	
	@Query("SELECT MAX(t.numeroTurma) FROM Turmas t")
    Integer findMaxNumeroTurmas();
	
	

    @Query("SELECT t FROM Turmas t")
	List<Turmas> findAllTurmas();
    
    List<Turmas> findByValidadedocursoBetween(Instant inicio, Instant fim);

    @Query("SELECT t FROM Turmas t " +
    	       "JOIN FETCH t.matricula m " +
    	       "JOIN FETCH m.funcionario f " +
    	       "JOIN FETCH f.empresa e " +
    	       "WHERE t.idTurmas = :id")
    	Optional<Turmas> findTurmaComEmpresa(@Param("id") UUID id);

    
 // No TurmasRepository, ajuste a consulta para usar JOIN FETCH
    @Query("SELECT DISTINCT t FROM Turmas t " +
           "LEFT JOIN FETCH t.matricula m " +
           "LEFT JOIN FETCH m.funcionario f " +
           "LEFT JOIN FETCH f.empresa " +
           "WHERE t.validadedocurso BETWEEN :hojeInicio AND :hojeFim")
    List<Turmas> findTurmasVencendoHojeComEmpresas(@Param("hojeInicio") Instant hojeInicio, @Param("hojeFim") Instant hojeFim);

    List<Turmas> findByDatafimBetween(Instant inicio, Instant fim);

}