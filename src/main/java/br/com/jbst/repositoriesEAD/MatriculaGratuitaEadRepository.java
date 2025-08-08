package br.com.jbst.repositoriesEAD;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.jbst.entities.MatriculasGratuita;

// Renomeado para evitar conflito
public interface MatriculaGratuitaEadRepository extends JpaRepository<MatriculasGratuita, UUID> {
}
