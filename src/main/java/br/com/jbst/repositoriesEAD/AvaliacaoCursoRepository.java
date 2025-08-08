package br.com.jbst.repositoriesEAD;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.AvaliacaoCurso;

public interface AvaliacaoCursoRepository  extends JpaRepository<AvaliacaoCurso, UUID> {

}
