package br.com.jbst.repositoriesEAD;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, UUID> {

}
