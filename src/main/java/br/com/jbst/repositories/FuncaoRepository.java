package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.Funcao;

public interface FuncaoRepository extends JpaRepository<Funcao, UUID> {

}
