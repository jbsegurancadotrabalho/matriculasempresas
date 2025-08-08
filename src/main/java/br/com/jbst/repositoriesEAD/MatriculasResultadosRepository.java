package br.com.jbst.repositoriesEAD;

import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.MatriculasResultados;

public interface MatriculasResultadosRepository extends JpaRepository<MatriculasResultados, UUID >{

}
