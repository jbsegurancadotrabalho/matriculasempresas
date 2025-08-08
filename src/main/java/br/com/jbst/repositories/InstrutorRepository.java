package br.com.jbst.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Instrutor;

public interface InstrutorRepository extends JpaRepository<Instrutor, UUID > {

	
	
	
	@Query("select i from Instrutor i where i.id = :id ")
	Instrutor find(@Param("id") UUID id);
	
    @Query("SELECT i FROM Instrutor i JOIN i.turmas t WHERE t.idTurmas = :idTurmas")
    List<Instrutor> findInstrutorComProficiencia(@Param("idTurmas") UUID idTurmas);
}
