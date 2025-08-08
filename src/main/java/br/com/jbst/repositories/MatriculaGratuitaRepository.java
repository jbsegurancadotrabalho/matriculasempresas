package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jbst.entities.MatriculasGratuita;


public interface MatriculaGratuitaRepository extends JpaRepository<MatriculasGratuita, UUID > {

	@Query("SELECT MAX(m.numeroMatriculaGratuita) FROM MatriculasGratuita m")
	Integer findMaxNumeroMatricula();

}
