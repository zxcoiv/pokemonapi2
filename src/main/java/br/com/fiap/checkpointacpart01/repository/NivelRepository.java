package br.com.fiap.checkpointacpart01.repository;

import br.com.fiap.checkpointacpart01.model.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {
}
