> *Microservices and Web Engineering* — Prof. Antonio Carlos de Lima Júnior

| | |
|---|---|
| **Repositório GitHub** | https://github.com/zxcoiv/pokemonapi2 |
| **Docker Hub** | https://hub.docker.com/r/zxcoiv/pokemonapi2 |

---

## 📚 Sumário

- [Tecnologias](#-tecnologias)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [1. Preparar o SQL Server](#1-preparar-o-sql-server)
- [2. Configurar a conexão](#2-configurar-a-conexão)
- [3. Executar a aplicação](#3-executar-a-aplicação)
- [4. Testar a API](#4-testar-a-api)
- [5. Conferir os dados no banco](#5-conferir-os-dados-no-banco)
- [Profiles](#-profiles)
- [Docker](#-docker)
- [Segurança](#-segurança)

---

## 🛠 Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 17 | Linguagem |
| Spring Boot | Framework da API REST |
| Spring Data JPA / Hibernate | Camada de persistência |
| Microsoft SQL Server | Banco de dados |
| `mssql-jdbc` | Driver JDBC do SQL Server |
| springdoc-openapi | Swagger UI |
| Maven | Build e dependências |
| Docker | Empacotamento e execução |

---

## 📁 Estrutura do projeto

```text
src/main/java/br/com/fiap/checkpointacpart01
├── Application.java              # Classe principal
├── controller/                   # Endpoints REST
│   ├── PokemonController.java
│   └── NivelController.java
├── model/                        # Entidades JPA (tabelas)
│   ├── Pokemon.java              # -> tabela pokemons
│   └── Nivel.java                # -> tabela niveis
└── repository/                   # Repositórios Spring Data JPA
    ├── PokemonRepository.java
    └── NivelRepository.java

src/main/resources
├── application.properties        # Profile default (SQL Server, cria/atualiza tabelas)
├── application-prd.properties    # Profile prd (SQL Server, não altera o schema)
└── migration.sql                 # Script T-SQL de criação do banco e tabelas
```

---

## ✅ Pré-requisitos

- Java 17
- Maven (ou o Maven Wrapper incluso: `mvnw` / `mvnw.cmd`)
- SQL Server acessível (local, em container ou remoto)
- Um cliente SQL para conferir os dados, como **DBeaver** ou Azure Data Studio
- Docker (opcional)

---

## 1. Preparar o SQL Server

### Subir um SQL Server local com Docker (opcional)

Se você ainda não tem um SQL Server disponível:

```sh
docker run -d --name sqlserver \
  -e "ACCEPT_EULA=Y" \
  -e "MSSQL_SA_PASSWORD=1q2w3e4R@" \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:2022-latest
```

### Criar o banco de dados

O SQL Server **não cria o database automaticamente**, então ele precisa existir antes de subir a API.

No **DBeaver**:

1. **Nova conexão → SQL Server**
   - Host: `localhost` · Porta: `1433`
   - Usuário: `sa` · Senha: `1q2w3e4R@`
   - Database: `master`
   - Em *Driver properties*, marque **trustServerCertificate = true**, se der erro de certificado.
2. Clique com o botão direito na conexão → **SQL Editor → New SQL script** e execute:

```sql
CREATE DATABASE api;
```

> No profile `default`, as tabelas `pokemons` e `niveis` são criadas automaticamente pelo Hibernate na primeira execução.
> No profile `prd`, execute o script [`migration.sql`](src/main/resources/migration.sql) antes de subir a aplicação.

---

## 2. Configurar a conexão

A conexão é configurada por **variáveis de ambiente**. No profile `default`, se uma variável não for definida, o valor padrão da tabela é usado.

| Variável | Descrição | Padrão |
|---|---|---|
| `DB_SERVER_URL` | Host do SQL Server | `localhost` |
| `DB_SERVER_PORT` | Porta do SQL Server | `1433` |
| `DB_SCHEMA` | Nome do database | `api` |
| `DB_USER` | Usuário | `sa` |
| `DB_PWD` | Senha | `1q2w3e4R@` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo | `default` |

URL JDBC montada pela aplicação:

```text
jdbc:sqlserver://${DB_SERVER_URL}:${DB_SERVER_PORT};databaseName=${DB_SCHEMA};encrypt=true;trustServerCertificate=true
```

### Usando o banco disponibilizado pelo professor

Basta apontar as variáveis para o servidor fornecido, **sem alterar código**.

**Linux / macOS**

```sh
export DB_SERVER_URL=<host-do-servidor>
export DB_SERVER_PORT=1433
export DB_SCHEMA=<nome-do-database>
export DB_USER=<usuario>
export DB_PWD=<senha>
```

**Windows PowerShell**

```powershell
$env:DB_SERVER_URL="<host-do-servidor>"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="<nome-do-database>"
$env:DB_USER="<usuario>"
$env:DB_PWD="<senha>"
```

---

## 3. Executar a aplicação

```sh
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API sobe em **http://localhost:8080**, e o Swagger fica na raiz dessa URL.

---

## 4. Testar a API

### Swagger UI

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/ |
| OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

### Endpoints de `/pokemon`

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/pokemon` | Cadastra um pokémon | `201 Created` |
| `GET` | `/pokemon` | Lista todos os pokémons | `200 OK` |
| `GET` | `/pokemon/{id}` | Busca um pokémon por id | `200` / `404` |
| `PUT` | `/pokemon/{id}` | Atualiza um pokémon | `200` / `404` |
| `DELETE` | `/pokemon/{id}` | Remove um pokémon | `204 No Content` |

```json
{
  "id": 1,
  "nome": "Charmander",
  "tipo": "Fogo",
  "tipoSecundario": null,
  "descricao": "Lagarto de fogo com chama na cauda"
}
```

### Endpoints de `/nivel`

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/nivel` | Cadastra um nível | `201 Created` |
| `GET` | `/nivel` | Lista todos os níveis | `200 OK` |
| `GET` | `/nivel/{id}` | Busca um nível por id | `200` / `404` |
| `PUT` | `/nivel/{id}` | Atualiza um nível | `200` / `404` |
| `DELETE` | `/nivel/{id}` | Remove um nível | `204 No Content` |

```json
{
  "id": 1,
  "nivel": 16,
  "nomeTreinador": "Ash Ketchum",
  "estagio": 2,
  "ondeEncontrar": "Rota 3 - Kanto"
}
```

### Roteiro rápido com `curl`

```sh
# Inserir
curl -X POST http://localhost:8080/pokemon \
  -H "Content-Type: application/json" \
  -d '{"id":1,"nome":"Charmander","tipo":"Fogo","tipoSecundario":null,"descricao":"Lagarto de fogo com chama na cauda"}'

# Consultar
curl http://localhost:8080/pokemon
curl http://localhost:8080/pokemon/1

# Alterar
curl -X PUT http://localhost:8080/pokemon/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Charmeleon","tipo":"Fogo","tipoSecundario":null,"descricao":"Evolucao do Charmander"}'

# Excluir
curl -X DELETE http://localhost:8080/pokemon/1
```

---

## 5. Conferir os dados no banco

Depois de usar os endpoints, confirme no SQL Server (DBeaver → SQL Editor) que as operações foram realmente gravadas:

```sql
SELECT * FROM api.dbo.pokemons;
SELECT * FROM api.dbo.niveis;
```

### Tabelas

**`pokemons`** → entidade `Pokemon`

| Coluna | Tipo | Nulo |
|---|---|---|
| `id` | `BIGINT` (PK) | não |
| `nome_pokemon` | `CHAR(24)` | não |
| `tipo` | `VARCHAR(255)` | não |
| `tipo_secundario` | `VARCHAR(255)` | sim |
| `descricao` | `VARCHAR(255)` | não |

**`niveis`** → entidade `Nivel`

| Coluna | Tipo | Nulo |
|---|---|---|
| `id` | `BIGINT` (PK) | não |
| `nivel_pokemon` | `INT` | não |
| `nome_treinador` | `VARCHAR(255)` | não |
| `estagio` | `INT` | não |
| `onde_encontrar` | `VARCHAR(255)` | não |

---

## ⚙ Profiles

O profile é escolhido pela variável `SPRING_PROFILES_ACTIVE`.

| | `default` | `prd` |
|---|---|---|
| Arquivo | `application.properties` | `application-prd.properties` |
| Banco | SQL Server | SQL Server |
| Tabelas | criadas/atualizadas pelo Hibernate (`ddl-auto=update`) | **não** alteradas (`ddl-auto=none`) — usar `migration.sql` |
| Variáveis de conexão | opcionais (têm valor padrão) | **obrigatórias** |
| `show-sql` | `true` | `false` |

Para usar o profile `prd`, aplique antes o script de criação do schema, abrindo o [`migration.sql`](src/main/resources/migration.sql) no DBeaver e executando com **Alt+X**, ou via `sqlcmd`:

```sh
sqlcmd -S localhost,1433 -U sa -P "1q2w3e4R@" -C -i src/main/resources/migration.sql
```

---

## 🐳 Docker

### Baixar ou gerar a imagem

```sh
docker pull zxcoiv/pokemonapi2:latest
# ou, a partir do código-fonte:
docker build -t pokemonapi2:1.0.0 .
```

### Executar o container

Se o SQL Server estiver na máquina host, use `host.docker.internal`:

```sh
docker run -d --name pokemon-api -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=1433 \
  -e DB_SCHEMA=api \
  -e DB_USER=sa \
  -e DB_PWD=1q2w3e4R@ \
  -e SPRING_PROFILES_ACTIVE=default \
  zxcoiv/pokemonapi2:latest
```

No Windows PowerShell:

```powershell
docker run -d --name pokemon-api -p 8080:8080 `
  -e DB_SERVER_URL=host.docker.internal `
  -e DB_SERVER_PORT=1433 `
  -e DB_SCHEMA=api `
  -e DB_USER=sa `
  -e DB_PWD=1q2w3e4R@ `
  -e SPRING_PROFILES_ACTIVE=default `
  zxcoiv/pokemonapi2:latest
```

> No Linux, pode ser necessário adicionar `--add-host=host.docker.internal:host-gateway`.
> Se API e SQL Server estiverem em containers na mesma rede Docker (`docker network create`), use o nome do container do banco como `DB_SERVER_URL`.

### Comandos úteis

```sh
docker ps                    # containers em execução
docker logs -f pokemon-api   # acompanhar logs
docker stop pokemon-api      # parar
docker rm pokemon-api        # remover
```

### Publicar no Docker Hub

```sh
docker login
docker tag pokemonapi2:1.0.0 zxcoiv/pokemonapi2:1.0.0
docker push zxcoiv/pokemonapi2:1.0.0
```

---

## 🔒 Segurança

As credenciais acima são **apenas de desenvolvimento local**. Não versione credenciais reais: use variáveis de ambiente ou um arquivo `.env` listado no `.gitignore`.

```env
DB_SERVER_URL=localhost
DB_SERVER_PORT=1433
DB_SCHEMA=api
DB_USER=sa
DB_PWD=1q2w3e4R@
SPRING_PROFILES_ACTIVE=default
```
# 🐉 Pokémon API — Check Point 2

API REST em **Java + Spring Boot** para cadastro de pokémons e de seus níveis, com persistência em **SQL Server** via **Spring Data JPA**, documentação **Swagger/OpenAPI**, configuração por **profiles** e execução com **Docker**.

> *Microservices and Web Engineering* — Prof. Antonio Carlos de Lima Júnior

| | |
|---|---|
| **Repositório GitHub** | https://github.com/zxcoiv/pokemonapi2 |
| **Docker Hub** | https://hub.docker.com/r/zxcoiv/pokemonapi2 |

---

## 📚 Sumário

- [Tecnologias](#-tecnologias)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [1. Preparar o SQL Server](#1-preparar-o-sql-server)
- [2. Configurar a conexão](#2-configurar-a-conexão)
- [3. Executar a aplicação](#3-executar-a-aplicação)
- [4. Testar a API](#4-testar-a-api)
- [5. Conferir os dados no banco](#5-conferir-os-dados-no-banco)
- [Profiles](#-profiles)
- [Docker](#-docker)
- [Segurança](#-segurança)

---

## 🛠 Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 17 | Linguagem |
| Spring Boot | Framework da API REST |
| Spring Data JPA / Hibernate | Camada de persistência |
| Microsoft SQL Server | Banco de dados |
| `mssql-jdbc` | Driver JDBC do SQL Server |
| springdoc-openapi | Swagger UI |
| Maven | Build e dependências |
| Docker | Empacotamento e execução |

---

## 📁 Estrutura do projeto

```text
src/main/java/br/com/fiap/checkpointacpart01
├── Application.java              # Classe principal
├── controller/                   # Endpoints REST
│   ├── PokemonController.java
│   └── NivelController.java
├── model/                        # Entidades JPA (tabelas)
│   ├── Pokemon.java              # -> tabela pokemons
│   └── Nivel.java                # -> tabela niveis
└── repository/                   # Repositórios Spring Data JPA
    ├── PokemonRepository.java
    └── NivelRepository.java

src/main/resources
├── application.properties        # Profile default (SQL Server, cria/atualiza tabelas)
├── application-prd.properties    # Profile prd (SQL Server, não altera o schema)
└── migration.sql                 # Script T-SQL de criação do banco e tabelas
```

---

## ✅ Pré-requisitos

- Java 17
- Maven (ou o Maven Wrapper incluso: `mvnw` / `mvnw.cmd`)
- SQL Server acessível (local, em container ou remoto)
- Um cliente SQL para conferir os dados, como **DBeaver** ou Azure Data Studio
- Docker (opcional)

---

## 1. Preparar o SQL Server

### Subir um SQL Server local com Docker (opcional)

Se você ainda não tem um SQL Server disponível:

```sh
docker run -d --name sqlserver \
  -e "ACCEPT_EULA=Y" \
  -e "MSSQL_SA_PASSWORD=1q2w3e4R@" \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:2022-latest
```

### Criar o banco de dados

O SQL Server **não cria o database automaticamente**, então ele precisa existir antes de subir a API.

No **DBeaver**:

1. **Nova conexão → SQL Server**
   - Host: `localhost` · Porta: `1433`
   - Usuário: `sa` · Senha: `1q2w3e4R@`
   - Database: `master`
   - Em *Driver properties*, marque **trustServerCertificate = true**, se der erro de certificado.
2. Clique com o botão direito na conexão → **SQL Editor → New SQL script** e execute:

```sql
CREATE DATABASE api;
```

> No profile `default`, as tabelas `pokemons` e `niveis` são criadas automaticamente pelo Hibernate na primeira execução.
> No profile `prd`, execute o script [`migration.sql`](src/main/resources/migration.sql) antes de subir a aplicação.

---

## 2. Configurar a conexão

A conexão é configurada por **variáveis de ambiente**. No profile `default`, se uma variável não for definida, o valor padrão da tabela é usado.

| Variável | Descrição | Padrão |
|---|---|---|
| `DB_SERVER_URL` | Host do SQL Server | `localhost` |
| `DB_SERVER_PORT` | Porta do SQL Server | `1433` |
| `DB_SCHEMA` | Nome do database | `api` |
| `DB_USER` | Usuário | `sa` |
| `DB_PWD` | Senha | `1q2w3e4R@` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo | `default` |

URL JDBC montada pela aplicação:

```text
jdbc:sqlserver://${DB_SERVER_URL}:${DB_SERVER_PORT};databaseName=${DB_SCHEMA};encrypt=true;trustServerCertificate=true
```

### Usando o banco disponibilizado pelo professor

Basta apontar as variáveis para o servidor fornecido, **sem alterar código**.

**Linux / macOS**

```sh
export DB_SERVER_URL=<host-do-servidor>
export DB_SERVER_PORT=1433
export DB_SCHEMA=<nome-do-database>
export DB_USER=<usuario>
export DB_PWD=<senha>
```

**Windows PowerShell**

```powershell
$env:DB_SERVER_URL="<host-do-servidor>"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="<nome-do-database>"
$env:DB_USER="<usuario>"
$env:DB_PWD="<senha>"
```

---

## 3. Executar a aplicação

```sh
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API sobe em **http://localhost:8080**, e o Swagger fica na raiz dessa URL.

---

## 4. Testar a API

### Swagger UI

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/ |
| OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

### Endpoints de `/pokemon`

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/pokemon` | Cadastra um pokémon | `201 Created` |
| `GET` | `/pokemon` | Lista todos os pokémons | `200 OK` |
| `GET` | `/pokemon/{id}` | Busca um pokémon por id | `200` / `404` |
| `PUT` | `/pokemon/{id}` | Atualiza um pokémon | `200` / `404` |
| `DELETE` | `/pokemon/{id}` | Remove um pokémon | `204 No Content` |

```json
{
  "id": 1,
  "nome": "Charmander",
  "tipo": "Fogo",
  "tipoSecundario": null,
  "descricao": "Lagarto de fogo com chama na cauda"
}
```

### Endpoints de `/nivel`

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `POST` | `/nivel` | Cadastra um nível | `201 Created` |
| `GET` | `/nivel` | Lista todos os níveis | `200 OK` |
| `GET` | `/nivel/{id}` | Busca um nível por id | `200` / `404` |
| `PUT` | `/nivel/{id}` | Atualiza um nível | `200` / `404` |
| `DELETE` | `/nivel/{id}` | Remove um nível | `204 No Content` |

```json
{
  "id": 1,
  "nivel": 16,
  "nomeTreinador": "Ash Ketchum",
  "estagio": 2,
  "ondeEncontrar": "Rota 3 - Kanto"
}
```

### Roteiro rápido com `curl`

```sh
# Inserir
curl -X POST http://localhost:8080/pokemon \
  -H "Content-Type: application/json" \
  -d '{"id":1,"nome":"Charmander","tipo":"Fogo","tipoSecundario":null,"descricao":"Lagarto de fogo com chama na cauda"}'

# Consultar
curl http://localhost:8080/pokemon
curl http://localhost:8080/pokemon/1

# Alterar
curl -X PUT http://localhost:8080/pokemon/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Charmeleon","tipo":"Fogo","tipoSecundario":null,"descricao":"Evolucao do Charmander"}'

# Excluir
curl -X DELETE http://localhost:8080/pokemon/1
```

---

## 5. Conferir os dados no banco

Depois de usar os endpoints, confirme no SQL Server (DBeaver → SQL Editor) que as operações foram realmente gravadas:

```sql
SELECT * FROM api.dbo.pokemons;
SELECT * FROM api.dbo.niveis;
```

### Tabelas

**`pokemons`** → entidade `Pokemon`

| Coluna | Tipo | Nulo |
|---|---|---|
| `id` | `BIGINT` (PK) | não |
| `nome_pokemon` | `CHAR(24)` | não |
| `tipo` | `VARCHAR(255)` | não |
| `tipo_secundario` | `VARCHAR(255)` | sim |
| `descricao` | `VARCHAR(255)` | não |

**`niveis`** → entidade `Nivel`

| Coluna | Tipo | Nulo |
|---|---|---|
| `id` | `BIGINT` (PK) | não |
| `nivel_pokemon` | `INT` | não |
| `nome_treinador` | `VARCHAR(255)` | não |
| `estagio` | `INT` | não |
| `onde_encontrar` | `VARCHAR(255)` | não |

---

## ⚙ Profiles

O profile é escolhido pela variável `SPRING_PROFILES_ACTIVE`.

| | `default` | `prd` |
|---|---|---|
| Arquivo | `application.properties` | `application-prd.properties` |
| Banco | SQL Server | SQL Server |
| Tabelas | criadas/atualizadas pelo Hibernate (`ddl-auto=update`) | **não** alteradas (`ddl-auto=none`) — usar `migration.sql` |
| Variáveis de conexão | opcionais (têm valor padrão) | **obrigatórias** |
| `show-sql` | `true` | `false` |

Para usar o profile `prd`, aplique antes o script de criação do schema, abrindo o [`migration.sql`](src/main/resources/migration.sql) no DBeaver e executando com **Alt+X**, ou via `sqlcmd`:

```sh
sqlcmd -S localhost,1433 -U sa -P "1q2w3e4R@" -C -i src/main/resources/migration.sql
```

---

## 🐳 Docker

### Baixar ou gerar a imagem

```sh
docker pull zxcoiv/pokemonapi2:latest
# ou, a partir do código-fonte:
docker build -t pokemonapi2:1.0.0 .
```

### Executar o container

Se o SQL Server estiver na máquina host, use `host.docker.internal`:

```sh
docker run -d --name pokemon-api -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=1433 \
  -e DB_SCHEMA=api \
  -e DB_USER=sa \
  -e DB_PWD=1q2w3e4R@ \
  -e SPRING_PROFILES_ACTIVE=default \
  zxcoiv/pokemonapi2:latest
```

No Windows PowerShell:

```powershell
docker run -d --name pokemon-api -p 8080:8080 `
  -e DB_SERVER_URL=host.docker.internal `
  -e DB_SERVER_PORT=1433 `
  -e DB_SCHEMA=api `
  -e DB_USER=sa `
  -e DB_PWD=1q2w3e4R@ `
  -e SPRING_PROFILES_ACTIVE=default `
  zxcoiv/pokemonapi2:latest
```

> No Linux, pode ser necessário adicionar `--add-host=host.docker.internal:host-gateway`.
> Se API e SQL Server estiverem em containers na mesma rede Docker (`docker network create`), use o nome do container do banco como `DB_SERVER_URL`.

### Comandos úteis

```sh
docker ps                    # containers em execução
docker logs -f pokemon-api   # acompanhar logs
docker stop pokemon-api      # parar
docker rm pokemon-api        # remover
```

### Publicar no Docker Hub

```sh
docker login
docker tag pokemonapi2:1.0.0 zxcoiv/pokemonapi2:1.0.0
docker push zxcoiv/pokemonapi2:1.0.0
```

---

## 🔒 Segurança

As credenciais acima são **apenas de desenvolvimento local**. Não versione credenciais reais: use variáveis de ambiente ou um arquivo `.env` listado no `.gitignore`.

```env
DB_SERVER_URL=localhost
DB_SERVER_PORT=1433
DB_SCHEMA=api
DB_USER=sa
DB_PWD=1q2w3e4R@
SPRING_PROFILES_ACTIVE=default
```
