package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.ProgressoAlunoTopico;
import br.com.jbst.entities.Topico;

public interface ProgressoAlunoTopicoRepository extends JpaRepository<ProgressoAlunoTopico, UUID> {
    Optional<ProgressoAlunoTopico> findByMatriculaAndTopico(Matriculas matricula, Topico topico);
    List<ProgressoAlunoTopico> findByMatricula(Matriculas matricula);
}