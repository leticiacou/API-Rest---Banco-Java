# ProjetoBanco — API REST

API REST de um sistema bancário desenvolvida em **Java 21** com **Spring Boot 4.0.5**.
Permite o cadastro de usuários e o gerenciamento de contas bancárias, incluindo operações de depósito, saque, transferência e exclusão de conta.

Os dados são persistidos em um banco **PostgreSQL** via **Spring Data JPA** (Hibernate).

---

## Sumário

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do banco de dados](#configuração-do-banco-de-dados)
- [Configuração da aplicação](#configuração-da-aplicação)
- [Como executar](#como-executar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Modelos de dados](#modelos-de-dados)
- [Endpoints](#endpoints)
  - [Usuários (`/user`)](#usuários-user)
  - [Contas (`/account`)](#contas-account)
- [Regras de negócio](#regras-de-negócio)
- [Tratamento de erros](#tratamento-de-erros)

---

## Tecnologias

| Tecnologia | Versão / Detalhes |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.5 (`spring-boot-starter-webmvc`) |
| Spring Data JPA | Hibernate com `ddl-auto: update` |
| Spring Security | BCrypt para hash de senhas; todas as rotas públicas |
| PostgreSQL | Driver 42.7.3 |
| Lombok | Geração automática de getters, setters, builders, construtores |
| Maven | 3.9+ (build e gerenciamento de dependências) |

---

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

1. **Java 21** (JDK)
   ```bash
   java -version
   # Esperado: openjdk version "21.x.x" ou similar
   ```
2. **Maven 3.9+**
   ```bash
   mvn -version
   ```
3. **PostgreSQL** (rodando e acessível em `localhost:5432`)
   ```bash
   psql --version
   ```

> **Dica:** Se você usa o [SDKMAN!](https://sdkman.io/), pode instalar o Java 21 com `sdk install java 21-open`.
> Para instalar o PostgreSQL no Ubuntu: `sudo apt update && sudo apt install postgresql postgresql-contrib`.

---

## Configuração do banco de dados

1. Inicie o serviço do PostgreSQL (caso ainda não esteja rodando):

   ```bash
   # Linux (systemd)
   sudo systemctl start postgresql

   # macOS (Homebrew)
   brew services start postgresql
   ```

2. Acesse o console do PostgreSQL e crie o banco de dados:

   ```bash
   sudo -u postgres psql
   ```

   ```sql
   CREATE DATABASE "bancoFicticio";
   -- (Opcional) crie um usuário específico:
   -- CREATE USER meu_usuario WITH PASSWORD 'minha_senha';
   -- GRANT ALL PRIVILEGES ON DATABASE "bancoFicticio" TO meu_usuario;
   \q
   ```

> As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto: update`) na primeira execução.

---

## Configuração da aplicação

O arquivo de configuração `src/main/resources/application.yaml` está no `.gitignore` por conter credenciais. Você precisa criá-lo manualmente.

Crie o arquivo `src/main/resources/application.yaml` com o seguinte conteúdo (ajuste os valores conforme o seu ambiente):

```yaml
server:
  port: 8082

spring:
  application:
    name: ProjetoBanco
  datasource:
    url: jdbc:postgresql://localhost:5432/bancoFicticio
    username: postgres
    password: SUA_SENHA_AQUI
  jpa:
    hibernate:
      ddl-auto: update
```

| Propriedade | Descrição |
|---|---|
| `server.port` | Porta em que a API vai rodar (padrão: `8082`) |
| `spring.datasource.url` | URL de conexão JDBC com o PostgreSQL |
| `spring.datasource.username` | Usuário do banco de dados |
| `spring.datasource.password` | Senha do banco de dados |
| `spring.jpa.hibernate.ddl-auto` | Estratégia de criação de tabelas (`update` cria/atualiza automaticamente) |

---

## Como executar

```bash
# 1. Clone o repositório
git clone https://github.com/leticiacou/API-Rest---Banco-Java.git
cd API-Rest---Banco-Java

# 2. Mude para a branch dev
git checkout dev

# 3. Crie o arquivo application.yaml (veja a seção acima)

# 4. Compile e execute a aplicação
mvn spring-boot:run
```

Se tudo estiver configurado corretamente, você verá no terminal:

```
Started ProjetoBancoApplication in X.XXX seconds
```

A API estará disponível em **`http://localhost:8082`**.

### Comandos úteis

```bash
# Compilar sem executar
mvn compile

# Executar os testes
mvn test

# Gerar o JAR executável
mvn package -DskipTests

# Rodar a partir do JAR gerado
java -jar target/ProjetoBanco-0.0.1-SNAPSHOT.jar
```

---

## Estrutura do projeto

```
src/main/java/br/com/leticiacouto/ProjetoBanco
├── ProjetoBancoApplication.java          # Classe principal (Spring Boot)
├── config
│   └── SecurityConfig.java               # Configuração do Spring Security
├── controller
│   ├── UserController.java               # Endpoints de usuários
│   └── AccountController.java            # Endpoints de contas
├── service
│   ├── UserService.java                  # Regras de negócio de usuários
│   └── AccountService.java               # Regras de negócio de contas
├── dto
│   ├── UserDto.java                      # Payload de criação/atualização de usuário
│   ├── DeleteUserDto.java                # Payload de exclusão de usuário
│   └── TransferDto.java                  # Payload de transferência entre contas
├── database
│   └── model
│       ├── UserEntity.java               # Entidade JPA — tabela "users"
│       └── AccountEntity.java            # Entidade JPA — tabela "accounts"
│   └── repository
│       ├── IUserRepository.java          # Repository JPA de usuários
│       └── IAccountRepository.java       # Repository JPA de contas
└── exceptions
    ├── AppException.java                 # Exceção base da aplicação
    ├── BusinessException.java            # Erros de regra de negócio (HTTP 400)
    ├── ResourceNotFoundException.java    # Recurso não encontrado (HTTP 404)
    ├── ErrorResponse.java                # Estrutura da resposta de erro
    └── handler
        └── GlobalExceptionHandler.java   # Handler global de exceções
```

---

## Modelos de dados

### UserEntity (tabela `users`)

| Campo    | Tipo     | Descrição |
|----------|----------|-----------|
| id       | `UUID`   | Identificador único gerado automaticamente |
| name     | `String` | Nome do usuário (mínimo 4 caracteres) |
| email    | `String` | E-mail (mínimo 8 caracteres, único) |
| password | `String` | Senha armazenada com hash BCrypt |

### AccountEntity (tabela `accounts`)

| Campo   | Tipo         | Descrição |
|---------|--------------|-----------|
| id      | `UUID`       | Identificador único gerado automaticamente |
| user    | `UserEntity` | Usuário dono da conta (relação `@OneToOne`) |
| balance | `double`     | Saldo atual da conta (inicia em `0.0`) |

### DTOs

**UserDto** — usado na criação e atualização de usuários:

```json
{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

**DeleteUserDto** — usado na exclusão de usuário (requer confirmação de senha):

```json
{
  "id": "uuid-do-usuario",
  "password": "senha-atual",
  "confirmPassword": "senha-atual"
}
```

**TransferDto** — usado para transferir valores entre contas:

```json
{
  "accountFrom": "uuid-da-conta-de-origem",
  "accountTo": "uuid-da-conta-de-destino",
  "amount": 100.0
}
```

---

## Endpoints

Base URL: `http://localhost:8082`

### Usuários (`/user`)

#### `POST /user` — Criar usuário

- **Status de sucesso:** `201 Created`
- **Body (JSON):** `UserDto`

```http
POST /user
Content-Type: application/json

{
  "name": "Leticia Couto",
  "email": "leticia@email.com",
  "password": "Senha@1234"
}
```

**Resposta:**

```json
{
  "id": "a1b2c3d4-...",
  "name": "Leticia Couto",
  "email": "leticia@email.com",
  "password": "$2a$10$..."
}
```

> A senha é retornada como hash BCrypt.

**Validações:**
- `name` — mínimo 4 caracteres.
- `email` — mínimo 8 caracteres e deve ser único.
- `password` — mínimo 8 caracteres e deve conter pelo menos **um caractere especial** (ex.: `@`, `#`, `!`, etc.).

---

#### `GET /user` — Listar todos os usuários

- **Status de sucesso:** `200 OK`

```json
[
  {
    "id": "a1b2c3d4-...",
    "name": "Leticia Couto",
    "email": "leticia@email.com",
    "password": "$2a$10$..."
  }
]
```

Se não houver usuários, retorna erro.

---

#### `GET /user/{id}` — Buscar usuário por ID

- **Status de sucesso:** `200 OK`
- **Parâmetro de rota:** `id` (`UUID`)

```http
GET /user/a1b2c3d4-5678-90ab-cdef-1234567890ab
```

---

#### `PUT /user/{id}` — Atualizar usuário

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (`UUID`)
- **Body (JSON):** `UserDto`

```http
PUT /user/a1b2c3d4-5678-90ab-cdef-1234567890ab
Content-Type: application/json

{
  "name": "Leticia C.",
  "email": "leticia.c@email.com",
  "password": "NovaSenha@123"
}
```

---

#### `DELETE /user/delete` — Excluir usuário e conta associada

- **Status de sucesso:** `204 No Content`
- **Body (JSON):** `DeleteUserDto`

```http
DELETE /user/delete
Content-Type: application/json

{
  "id": "a1b2c3d4-5678-90ab-cdef-1234567890ab",
  "password": "Senha@1234",
  "confirmPassword": "Senha@1234"
}
```

**Validações:**
- O usuário deve existir.
- A senha deve estar correta.
- `password` e `confirmPassword` devem ser iguais.

---

### Contas (`/account`)

#### `POST /account/{userId}` — Criar conta

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `userId` (`UUID` do usuário)

```http
POST /account/a1b2c3d4-5678-90ab-cdef-1234567890ab
```

**Resposta:**

```json
{
  "id": "f0b8c9a2-1234-4a2b-8fa1-abcdef012345",
  "user": {
    "id": "a1b2c3d4-...",
    "name": "Leticia Couto",
    "email": "leticia@email.com",
    "password": "$2a$10$..."
  },
  "balance": 0.0
}
```

**Validações:**
- O usuário informado deve existir.

---

#### `GET /account/{id}` — Consultar saldo

- **Status de sucesso:** `200 OK`
- **Parâmetro de rota:** `id` (`UUID` da conta)

```http
GET /account/f0b8c9a2-1234-4a2b-8fa1-abcdef012345
```

**Resposta:**

```json
150.0
```

---

#### `PUT /account/{id}` — Depósito

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (`UUID` da conta)
- **Body (JSON):** valor numérico (`Double`)

```http
PUT /account/f0b8c9a2-1234-4a2b-8fa1-abcdef012345
Content-Type: application/json

100.0
```

**Resposta:** novo saldo.

```json
100.0
```

**Validações:** o valor deve ser `>= 0`.

---

#### `PUT /account/withdraw/{id}` — Saque

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (`UUID` da conta)
- **Body (JSON):** valor numérico (`Double`)

```http
PUT /account/withdraw/f0b8c9a2-1234-4a2b-8fa1-abcdef012345
Content-Type: application/json

50.0
```

**Resposta:** novo saldo.

```json
50.0
```

**Validações:**
- O valor deve ser `>= 0`.
- O valor não pode ser superior ao saldo disponível.

---

#### `PUT /account/transfer` — Transferência entre contas

- **Status de sucesso:** `201 Created`
- **Body (JSON):** `TransferDto`

```http
PUT /account/transfer
Content-Type: application/json

{
  "accountFrom": "f0b8c9a2-1234-4a2b-8fa1-abcdef012345",
  "accountTo": "aa11bb22-3333-4444-5555-66778899aabb",
  "amount": 30.0
}
```

**Resposta:**

```
O saldo atualizado após a transferencia é 70.0
```

**Validações:**
- As duas contas devem existir.
- O saldo da conta de origem deve ser suficiente.

---

## Regras de negócio

### Usuários

- `name` com mínimo de **4 caracteres**.
- `email` com mínimo de **8 caracteres** e **único** (não pode haver dois usuários com o mesmo e-mail).
- `password` com mínimo de **8 caracteres** e pelo menos **um caractere especial**.
- A senha é armazenada com **hash BCrypt** (nunca em texto puro).
- Para excluir um usuário, é necessário confirmar a senha.

### Contas

- Cada conta está associada a exatamente **um** usuário (relação `@OneToOne`).
- O saldo inicial de uma nova conta é `0.0`.
- Não é permitido depositar ou sacar valores negativos.
- Não é permitido sacar ou transferir mais do que o saldo disponível.
- Ao excluir um usuário, a conta associada também é removida.

---

## Tratamento de erros

A aplicação possui um `GlobalExceptionHandler` que retorna respostas padronizadas:

```json
{
  "status": 400,
  "message": "Descrição do erro",
  "timestamp": "2025-01-01T12:00:00"
}
```

| Código HTTP | Tipo de exceção | Quando ocorre |
|---|---|---|
| `400 Bad Request` | `BusinessException` | Violação de regra de negócio |
| `404 Not Found` | `ResourceNotFoundException` | Recurso não encontrado |
| `500 Internal Server Error` | `Exception` | Erro inesperado |

### Principais mensagens de erro

| Contexto | Mensagem |
|---|---|
| Senha fraca | `Erro ao criar a senha, insira no mínimo 8 caracteres` |
| Senha sem caractere especial | `Erro ao criar o usuário, a senha precisa conter pelo menos um caractere especial` |
| E-mail inválido | `Erro ao criar o usuário, email inválido` |
| E-mail duplicado | `Email já cadastrado` |
| Nome inválido | `Erro ao criar o usuário, nome inválido` |
| Nenhum usuário encontrado | `Nenhum user encontrado` |
| Usuário não encontrado | `Usuário não encontrado` |
| Conta não encontrada | `Essa conta não existe` |
| Conta origem não encontrada | `Conta origem não encontrada` |
| Conta destino não encontrada | `Conta de destino não encontrada` |
| Valor negativo | `Valor inválido` |
| Saldo insuficiente (saque) | `Valor inválido, saldo insuficiente` |
| Saldo insuficiente (transferência) | `Saldo insuficiente` |
| Senha incorreta (exclusão) | `Senha incorreta` |
| Senhas não coincidem (exclusão) | `As senhas não coincidem` |
