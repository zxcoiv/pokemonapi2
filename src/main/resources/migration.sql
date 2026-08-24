-- Script de criacao manual do schema para o profile prd.
-- No profile prd a aplicacao NAO cria banco nem tabelas (ddl-auto=none),
-- portanto este script deve ser aplicado manualmente antes de subir o container.
--
-- Uso:
--   docker exec -i <container_mysql> mysql -uroot -p<senha> < src/main/resources/migration.sql
--   ou: mysql -h <host> -P <porta> -u <usuario> -p < src/main/resources/migration.sql

create database if not exists pokemondb;

use pokemondb;

create table pokemons (
    id bigint not null,
    nome_pokemon char(24) not null,
    tipo varchar(255) not null,
    tipo_secundario varchar(255),
    descricao varchar(255) not null,
    primary key (id)
) engine = InnoDB;

create table niveis (
    id bigint not null,
    nivel_pokemon integer not null,
    nome_treinador varchar(255) not null,
    estagio integer not null,
    onde_encontrar varchar(255) not null,
    primary key (id)
) engine = InnoDB;
