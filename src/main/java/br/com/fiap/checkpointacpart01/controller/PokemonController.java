package br.com.fiap.checkpointacpart01.controller;

import br.com.fiap.checkpointacpart01.model.Pokemon;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.fiap.checkpointacpart01.repository.PokemonRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("pokemon")
public class PokemonController {
    private PokemonRepository repository;

    public PokemonController(PokemonRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Pokemon> create(@RequestBody Pokemon pokemon) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(pokemon));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findById(@PathVariable Long id) {
        return repository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pokemon> update(@PathVariable Long id, @RequestBody Pokemon pokemon) {
        Optional<Pokemon> optPokemon = repository.findById(id);

        if (optPokemon.isPresent()) {
            pokemon.setId(id);
            Pokemon pokemonAlterado = repository.save(pokemon);
            return ResponseEntity.ok(pokemonAlterado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
