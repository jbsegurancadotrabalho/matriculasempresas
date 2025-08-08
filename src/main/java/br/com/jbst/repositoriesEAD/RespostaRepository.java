package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.RespostaEad;

public interface RespostaRepository extends JpaRepository <RespostaEad, UUID>{

	List<RespostaEad> findByPergunta_IdPergunta(UUID idPergunta);

}
