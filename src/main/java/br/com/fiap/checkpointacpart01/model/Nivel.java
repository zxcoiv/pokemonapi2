package br.com.fiap.checkpointacpart01.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "niveis")
public class Nivel {
    @Id
    private Long id;

    @Column(name = "nivel_pokemon", nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private String nomeTreinador;

    @Column(nullable = false)
    private Integer estagio;

    @Column(nullable = false)
    private String ondeEncontrar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getNomeTreinador() {
        return nomeTreinador;
    }

    public void setNomeTreinador(String nomeTreinador) {
        this.nomeTreinador = nomeTreinador;
    }

    public Integer getEstagio() {
        return estagio;
    }

    public void setEstagio(Integer estagio) {
        this.estagio = estagio;
    }

    public String getOndeEncontrar() {
        return ondeEncontrar;
    }

    public void setOndeEncontrar(String ondeEncontrar) {
        this.ondeEncontrar = ondeEncontrar;
    }
}
