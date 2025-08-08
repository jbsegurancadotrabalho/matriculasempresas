package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, UUID> {

	List<Pergunta> findByAvaliacao_IdAvaliacaoCurso(UUID idAvaliacaoCurso);

	List<Pergunta> findByTopicoIdTopico(UUID idTopico);


}
