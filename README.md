# PokemonApiCp2

API REST desenvolvida em **Spring Boot** para cadastro de pokemons e de seus niveis, com acesso a banco de dados MySQL, documentacao **Swagger/OpenAPI**, configuracao por **profiles** e execucao com **Docker**.

Check Point 1 — *Microservices and Web Engineering* — Prof. Antonio Carlos de Lima Junior.

- Repositorio GitHub: https://github.com/MuriloMercadante/PokemonApiCp2
- Repositorio Docker Hub: https://hub.docker.com/r/murilomercadante/pokemonapicp2

---

## Pre-requisitos

Para executar o projeto localmente, voce vai precisar ter instalado:

- Java 17
- Maven
- MySQL
- Docker (opcional)

---

## Execucao local

### 1. Configuracao das variaveis de ambiente

A aplicacao utiliza variaveis de ambiente para configurar a conexao com o banco de dados e o profile do Spring Boot.

| Variavel | Descricao | Exemplo |
|---|---|---|
| `DB_SERVER_URL` | Endereco do servidor do banco de dados | `localhost` |
| `DB_SERVER_PORT` | Porta do banco de dados | `3306` |
| `DB_SCHEMA` | Nome do schema | `pokemondb` |
| `DB_USER` | Usuario do banco de dados | `root` |
| `DB_PWD` | Senha do banco de dados | `root_pwd` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo do Spring Boot | `default` |

No profile `default`, se as variaveis nao forem definidas, a aplicacao usa valores padrao (`host.docker.internal`, `3306`, `pokemondb`, `root`, `root_pwd`).

#### Linux / macOS

```sh
export DB_SERVER_URL=localhost
export DB_SERVER_PORT=3306
export DB_SCHEMA=pokemondb
export DB_USER=root
export DB_PWD=root_pwd
export SPRING_PROFILES_ACTIVE=default
```

#### Windows PowerShell

```powershell
$env:DB_SERVER_URL="localhost"
$env:DB_SERVER_PORT="3306"
$env:DB_SCHEMA="pokemondb"
$env:DB_USER="root"
$env:DB_PWD="root_pwd"
$env:SPRING_PROFILES_ACTIVE="default"
```

### 2. Executar a aplicacao

Com Maven:

```sh
mvn spring-boot:run
```

Ou utilizando o Maven Wrapper:

```sh
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicacao sera iniciada em:

```text
http://localhost:8080
```

---

## Execucao com Docker

### 1. Baixar a imagem do Docker Hub

```sh
docker pull murilomercadante/pokemonapicp2:1.0.0
```

Tambem esta disponivel a tag `latest`:

```sh
docker pull murilomercadante/pokemonapicp2:latest
```

Ou, para gerar a imagem localmente a partir do codigo-fonte:

```sh
docker build -t pokemonapicp2:1.0.0 .
```

### 2. Executar o container

Caso o banco de dados esteja sendo executado na maquina host, utilize `host.docker.internal` para permitir que o container acesse o banco.

```sh
docker run -d \
  --name pokemon-api \
  -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=pokemondb \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=default \
  murilomercadante/pokemonapicp2:1.0.0
```

No Windows PowerShell:

```powershell
docker run -d `
  --name pokemon-api `
  -p 8080:8080 `
  -e DB_SERVER_URL=host.docker.internal `
  -e DB_SERVER_PORT=3306 `
  -e DB_SCHEMA=pokemondb `
  -e DB_USER=root `
  -e DB_PWD=root_pwd `
  -e SPRING_PROFILES_ACTIVE=default `
  murilomercadante/pokemonapicp2:1.0.0
```

A aplicacao ficara disponivel em:

```text
http://localhost:8080
```

> **Nota:** `host.docker.internal` permite que o container acesse servicos executados na maquina host. Em ambientes Linux, dependendo da configuracao do Docker, pode ser necessario utilizar uma configuracao de rede diferente (ex.: `--add-host=host.docker.internal:host-gateway`), ou apontar `DB_SERVER_URL` para o nome do container do MySQL caso ambos estejam na mesma rede Docker (`docker network create` + `--network`).

### 3. Validar

```sh
curl http://localhost:8080/pokemon
```

---

## Profiles do Spring Boot

O profile ativo da aplicacao e definido atraves da variavel de ambiente `SPRING_PROFILES_ACTIVE`. A aplicacao possui dois profiles:

### `default`

Profile padrao, usado quando `SPRING_PROFILES_ACTIVE` nao e definida ou e definida como `default`. Configurado em `src/main/resources/application.properties`.

- Cria o banco de dados automaticamente, se ele nao existir (`createDatabaseIfNotExist=true`).
- Cria/atualiza as tabelas automaticamente (`spring.jpa.hibernate.ddl-auto=update`).
- `spring.jpa.show-sql=true`.

```sh
export SPRING_PROFILES_ACTIVE=default
```

### `prd`

Profile de producao, configurado em `src/main/resources/application-prd.properties`.

- **Nao** cria o banco de dados nem as tabelas automaticamente (`spring.jpa.hibernate.ddl-auto=none`, sem `createDatabaseIfNotExist`).
- O banco e as tabelas precisam existir **antes** de a aplicacao subir — use o script [`src/main/resources/migration.sql`](src/main/resources/migration.sql).
- `spring.jpa.show-sql=false`.
- Todas as variaveis de conexao (`DB_SERVER_URL`, `DB_SERVER_PORT`, `DB_SCHEMA`, `DB_USER`, `DB_PWD`) sao obrigatorias, sem valor padrao.

```sh
export SPRING_PROFILES_ACTIVE=prd
```

Ao executar com Docker:

```sh
docker run -d \
  --name pokemon-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prd \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=pokemondb \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  murilomercadante/pokemonapicp2:1.0.0
```

#### Criando o schema para o profile `prd`

Antes de subir a aplicacao no profile `prd`, aplique o script de criacao das tabelas em um MySQL acessivel:

```sh
mysql -h <host> -P <porta> -u <usuario> -p < src/main/resources/migration.sql
```

Ou, se o MySQL estiver rodando em um container:

```sh
docker exec -i <container_mysql> mysql -uroot -p<senha> < src/main/resources/migration.sql
```

---

## Swagger / OpenAPI

Com a aplicacao em execucao (qualquer profile), a documentacao Swagger fica disponivel na raiz:

```text
http://localhost:8080/
```

Especificacao OpenAPI (JSON):

```text
http://localhost:8080/v3/api-docs
```

---

## Endpoints da API

### `/pokemon`

| Metodo | Rota | Descricao |
|---|---|---|
| `POST` | `/pokemon` | Cadastra um pokemon |
| `GET` | `/pokemon` | Lista todos os pokemons |
| `GET` | `/pokemon/{id}` | Busca um pokemon por id |
| `PUT` | `/pokemon/{id}` | Atualiza um pokemon |
| `DELETE` | `/pokemon/{id}` | Remove um pokemon |

```jsonc
// POST /pokemon
{
  "id": 1,
  "nome": "Charmander",
  "tipo": "Fogo",
  "tipoSecundario": null,
  "descricao": "Lagarto de fogo com chama na cauda"
}
```

### `/nivel`

| Metodo | Rota | Descricao |
|---|---|---|
| `POST` | `/nivel` | Cadastra um nivel |
| `GET` | `/nivel` | Lista todos os niveis |
| `GET` | `/nivel/{id}` | Busca um nivel por id |
| `PUT` | `/nivel/{id}` | Atualiza um nivel |
| `DELETE` | `/nivel/{id}` | Remove um nivel |

```jsonc
// POST /nivel
{
  "id": 1,
  "nivel": 16,
  "nomeTreinador": "Ash Ketchum",
  "estagio": 2,
  "ondeEncontrar": "Rota 3 - Kanto"
}
```

---

## Docker — comandos uteis

### Criar a imagem

```sh
docker build -t pokemonapicp2:1.0.0 .
```

### Executar o container

```sh
docker run -d \
  --name pokemon-api \
  -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=pokemondb \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=default \
  pokemonapicp2:1.0.0
```

### Publicar no Docker Hub

```sh
docker login
docker tag pokemonapicp2:1.0.0 murilomercadante/pokemonapicp2:1.0.0
docker push murilomercadante/pokemonapicp2:1.0.0
```

### Listar containers em execucao

```sh
docker ps
```

### Parar e remover o container

```sh
docker stop pokemon-api
docker rm pokemon-api
```

### Ver logs

```sh
docker logs -f pokemon-api
```

---

## Seguranca

Nao versione credenciais reais no repositorio.

Recomenda-se utilizar um arquivo `.env` local para desenvolvimento e adiciona-lo ao `.gitignore`:

```gitignore
.env
```

Exemplo de conteudo:

```env
DB_SERVER_URL=localhost
DB_SERVER_PORT=3306
DB_SCHEMA=pokemondb
DB_USER=root
DB_PWD=root_pwd
SPRING_PROFILES_ACTIVE=default
```
