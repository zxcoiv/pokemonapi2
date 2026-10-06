IF DB_ID('api') IS NULL CREATE DATABASE api;
GO
USE api;
GO
CREATE TABLE pokemons (
                          id BIGINT NOT NULL PRIMARY KEY,
                          nome_pokemon CHAR(24) NOT NULL,
                          tipo VARCHAR(255) NOT NULL,
                          tipo_secundario VARCHAR(255),
                          descricao VARCHAR(255) NOT NULL
);
CREATE TABLE niveis (
                        id BIGINT NOT NULL PRIMARY KEY,
                        nivel_pokemon INT NOT NULL,
                        nome_treinador VARCHAR(255) NOT NULL,
                        estagio INT NOT NULL,
                        onde_encontrar VARCHAR(255) NOT NULL
);