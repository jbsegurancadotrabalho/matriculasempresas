package br.com.jbst.repositoriesEAD;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.MatriculasGratuita;
import br.com.jbst.entities.ProgressoAlunoTopicoGratuito;
import br.com.jbst.entities.Topico;

public interface ProgressoAlunoTopicoGratuitoRepository extends JpaRepository<ProgressoAlunoTopicoGratuito, UUID> {
    Optional<ProgressoAlunoTopicoGratuito> findByMatriculasGratuitaAndTopico(MatriculasGratuita matriculasGratuita, Topico topico);
    List<ProgressoAlunoTopicoGratuito> findByMatriculasGratuita(MatriculasGratuita matriculasGratuita);
}
