package br.com.jbst.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Empresa;

public interface EmpresaRepository extends JpaRepository <Empresa, UUID>{

	@Query("""
	        SELECT DISTINCT m.funcionario.empresa
	        FROM Matriculas m
	        WHERE m.turmas.idTurmas = :idTurma
	        AND m.funcionario IS NOT NULL
	        AND m.funcionario.empresa IS NOT NULL
	    """)
	    List<Empresa> findDistinctByFuncionarios_Matriculas_Turmas_IdTurmas(@Param("idTurma") UUID idTurma);
	}
