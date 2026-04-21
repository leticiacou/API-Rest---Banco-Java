# ProjetoBanco - API REST

API REST de um sistema bancário simples desenvolvida em **Java 21** com **Spring Boot**. A aplicação permite o cadastro de usuários e o gerenciamento de contas bancárias, incluindo operações de depósito, saque e transferência entre contas.

> Os dados são mantidos em memória (não há persistência em banco de dados real). Ao reiniciar a aplicação, os registros são perdidos.

---

## Sumário

- [Tecnologias](#tecnologias)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como executar](#como-executar)
- [Modelos de dados](#modelos-de-dados)
- [Endpoints](#endpoints)
  - [Usuários (`/user`)](#usuários-user)
  - [Contas (`/account`)](#contas-account)
- [Regras de negócio](#regras-de-negócio)
- [Erros](#erros)

---

## Tecnologias

- **Java 21**
- **Spring Boot 4.0.5** (`spring-boot-starter-webmvc`)
- **Lombok** (geração automática de getters, setters, builders, construtores)
- **Maven** (build e gerenciamento de dependências)

---

## Estrutura do projeto

```
src/main/java/br/com/leticiacouto/ProjetoBanco
├── ProjetoBancoApplication.java        # Classe principal (Spring Boot)
├── controller
│   ├── UserController.java             # Endpoints de usuários
│   └── AccountController.java          # Endpoints de contas
├── service
│   ├── UserService.java                # Regras de negócio de usuários
│   └── AccountService.java             # Regras de negócio de contas
├── dto
│   ├── UserDto.java                    # Payload de entrada de usuário
│   └── TransferDto.java                # Payload de transferência
└── database
    ├── model
    │   ├── User.java                   # Entidade Usuário
    │   └── Account.java                # Entidade Conta
    └── repository
        └── Database.java               # "Banco" em memória (Sets de User e Account)
```

---

## Como executar

Pré-requisitos:

- Java 21
- Maven 3.9+ (ou use o wrapper, se adicionado ao repositório)

```bash
# Clonar o repositório
git clone https://github.com/leticiacou/API-Rest---Banco-Java.git
cd API-Rest---Banco-Java

# Executar a aplicação
mvn spring-boot:run
```

A API sobe, por padrão, em `http://localhost:8080`.

---

## Modelos de dados

### User

| Campo    | Tipo     | Descrição                               |
|----------|----------|-----------------------------------------|
| id       | `int`    | Identificador sequencial gerado pela API |
| name     | `String` | Nome do usuário (mínimo 4 caracteres)   |
| email    | `String` | E-mail (mínimo 8 caracteres)            |
| password | `String` | Senha (mínimo 8 caracteres)             |

### Account

| Campo   | Tipo     | Descrição                                        |
|---------|----------|--------------------------------------------------|
| id      | `UUID`   | Identificador único da conta (gerado pela API)   |
| userId  | `int`    | ID do usuário dono da conta                      |
| balance | `double` | Saldo atual da conta (inicia em `0`)             |

### UserDto

Payload usado na criação/atualização de usuários.

```json
{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

### TransferDto

Payload usado para transferir valores entre contas.

```json
{
  "accountFrom": "uuid-da-conta-de-origem",
  "accountTo":   "uuid-da-conta-de-destino",
  "amount": 100.0
}
```

---

## Endpoints

Base URL: `http://localhost:8080`

### Usuários (`/user`)

#### `POST /user`
Cria um novo usuário.

- **Status de sucesso:** `201 Created`
- **Body (JSON):** `UserDto`

**Exemplo de requisição:**

```http
POST /user
Content-Type: application/json

{
  "name": "Leticia Couto",
  "email": "leticia@email.com",
  "password": "senha1234"
}
```

**Exemplo de resposta:**

```json
{
  "id": 1,
  "name": "Leticia Couto",
  "email": "leticia@email.com",
  "password": "senha1234"
}
```

**Validações:**
- `password` deve ter pelo menos 8 caracteres.
- `email` deve ter pelo menos 8 caracteres.
- `name` deve ter pelo menos 4 caracteres.

---

#### `GET /user`
Lista todos os usuários cadastrados.

- **Status de sucesso:** `200 OK`
- **Resposta:** array (Set) de `User`.

**Exemplo de resposta:**

```json
[
  {
    "id": 1,
    "name": "Leticia Couto",
    "email": "leticia@email.com",
    "password": "senha1234"
  }
]
```

Se não houver usuários cadastrados, é lançado um erro ("Não existem usuários no banco de dados").

---

#### `GET /user/{id}`
Busca um usuário pelo ID numérico.

- **Status de sucesso:** `200 OK`
- **Parâmetro de rota:** `id` (int)

**Exemplo:**

```http
GET /user/1
```

Se o usuário não existir, é lançado um erro ("Usuário não encontrado").

---

#### `PUT /user/{id}`
Atualiza os dados de um usuário existente.

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (int)
- **Body (JSON):** `UserDto`

**Exemplo de requisição:**

```http
PUT /user/1
Content-Type: application/json

{
  "name": "Leticia C.",
  "email": "leticia.c@email.com",
  "password": "novaSenha123"
}
```

**Exemplo de resposta:**

```json
{
  "id": 1,
  "name": "Leticia C.",
  "email": "leticia.c@email.com",
  "password": "novaSenha123"
}
```

---

### Contas (`/account`)

#### `POST /account/{id}`
Cria uma nova conta para o usuário cujo `id` é passado na URL.

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (int) — ID do usuário dono da conta.

**Exemplo de requisição:**

```http
POST /account/1
```

**Exemplo de resposta:**

```json
{
  "id": "f0b8c9a2-1234-4a2b-8fa1-abcdef012345",
  "userId": 1,
  "balance": 0.0
}
```

**Validações:**
- O usuário informado deve existir.
- Cada usuário pode ter apenas **uma** conta ativa.

---

#### `GET /account/{id}`
Consulta o saldo de uma conta pelo seu UUID.

- **Status de sucesso:** `200 OK`
- **Parâmetro de rota:** `id` (UUID)

**Exemplo de requisição:**

```http
GET /account/f0b8c9a2-1234-4a2b-8fa1-abcdef012345
```

**Exemplo de resposta:**

```json
150.0
```

---

#### `PATCH /account/{id}` — Depósito
Deposita um valor em uma conta.

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (UUID)
- **Body (JSON):** valor numérico (`Double`).

**Exemplo de requisição:**

```http
PATCH /account/f0b8c9a2-1234-4a2b-8fa1-abcdef012345
Content-Type: application/json

100.0
```

**Resposta:** novo saldo da conta.

```json
100.0
```

**Validações:** o valor deve ser maior ou igual a `0`.

---

#### `PATCH /account/withdraw/{id}` — Saque
Retira um valor da conta.

- **Status de sucesso:** `201 Created`
- **Parâmetro de rota:** `id` (UUID)
- **Body (JSON):** valor numérico (`Double`).

**Exemplo de requisição:**

```http
PATCH /account/withdraw/f0b8c9a2-1234-4a2b-8fa1-abcdef012345
Content-Type: application/json

50.0
```

**Resposta:** novo saldo da conta.

```json
50.0
```

**Validações:**
- O valor deve ser maior ou igual a `0`.
- O valor não pode ser superior ao saldo disponível.

---

#### `PATCH /account/transfer` — Transferência
Transfere um valor entre duas contas.

- **Status de sucesso:** `201 Created`
- **Body (JSON):** `TransferDto`

**Exemplo de requisição:**

```http
PATCH /account/transfer
Content-Type: application/json

{
  "accountFrom": "f0b8c9a2-1234-4a2b-8fa1-abcdef012345",
  "accountTo":   "aa11bb22-3333-4444-5555-66778899aabb",
  "amount": 30.0
}
```

**Exemplo de resposta:**

```
O saldo atualizado após a transferencia é 70.0
```

**Validações:**
- As duas contas devem existir.
- O saldo da conta de origem deve ser suficiente para cobrir o valor transferido.

---

## Regras de negócio

- **Usuários:**
  - `name` com mínimo de 4 caracteres.
  - `email` com mínimo de 8 caracteres.
  - `password` com mínimo de 8 caracteres.
  - Cada usuário recebe um `id` sequencial (gerado a partir do maior `id` existente + 1).

- **Contas:**
  - Cada conta está associada a exatamente **um** usuário, identificado por `userId`.
  - Um usuário só pode ter **uma** conta ativa.
  - O saldo inicial de uma nova conta é `0.0`.
  - Não é permitido depositar ou sacar valores negativos.
  - Não é permitido sacar ou transferir mais do que o saldo disponível.

---

## Erros

Atualmente a aplicação lança exceções diretamente a partir das camadas de serviço (ex.: `RuntimeException`, `Error`) com mensagens em português. Não há um handler global de exceções configurado, então a resposta padrão para falhas é um `500 Internal Server Error` contendo a mensagem do erro.

Principais mensagens de erro:

| Contexto                         | Mensagem                                              |
|----------------------------------|-------------------------------------------------------|
| Criação de usuário               | `Erro ao criar o usuário, senha fraca`                |
| Criação de usuário               | `Erro ao criar o usuário, email inválido`             |
| Criação de usuário               | `Erro ao criar o usuário, nome inválido`              |
| Listagem de usuários vazia       | `Não existem usuários no banco de dados`              |
| Usuário não encontrado           | `Usuário não encontrado`                              |
| Criação de conta                 | `A conta já existe`                                   |
| Criação de conta                 | `O usuário não foi encontrado`                        |
| Criação de conta                 | `O usuário já tem uma conta ativa`                    |
| Consulta/operação em conta       | `Conta não encontrada`                                |
| Depósito/saque                   | `Valor inválido`                                      |
| Transferência                    | `Saldo insuficiente`                                  |
