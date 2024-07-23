package br.com.jbst.repositories;


import java.util.List;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.DTO.GetMatriculasDTO;
import br.com.jbst.entities.Matriculas;

public interface MatriculasRepository extends JpaRepository<Matriculas, UUID > {

	
	@Query("SELECT MAX(m.numeroMatricula) FROM Matriculas m")
    Integer findMaxNumeroMatricula();
	
	
	@Query(
			"select m from Matriculas m "
		  + "order by m.numeroMatricula"
	)
	List<Matriculas> findAllMatriculas();
	
	
	 @Query("SELECT m FROM Matriculas m WHERE MONTH(m.dataHoraCriacao) = :mes AND m.funcionario IS NOT NULL")
	    List<Matriculas> findMatriculasFuncionariosByMes(@Param("mes") int mes);
	 
	 
	 
	  @Query("SELECT m FROM Matriculas m JOIN m.usuarios u WHERE u.id = :usuarioId")
	    List<Matriculas> findMatriculasByUsuarioId(UUID usuarioId);
	}
	 


