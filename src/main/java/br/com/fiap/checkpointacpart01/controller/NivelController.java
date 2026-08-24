package br.com.fiap.checkpointacpart01.controller;

import br.com.fiap.checkpointacpart01.model.Nivel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.fiap.checkpointacpart01.repository.NivelRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("nivel")
public class NivelController {
    private NivelRepository repository;

    public NivelController(NivelRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Nivel> create(@RequestBody Nivel nivel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(nivel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nivel> findById(@PathVariable Long id) {
        return repository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Nivel>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nivel> update(@PathVariable Long id, @RequestBody Nivel nivel) {
        Optional<Nivel> optNivel = repository.findById(id);

        if (optNivel.isPresent()) {
            nivel.setId(id);
            Nivel nivelAlterado = repository.save(nivel);
            return ResponseEntity.ok(nivelAlterado);
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
