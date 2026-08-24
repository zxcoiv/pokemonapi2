package br.com.fiap.checkpointacpart01.repository;

import br.com.fiap.checkpointacpart01.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
}
