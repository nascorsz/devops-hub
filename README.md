# Hub Conecta - Plataforma de Vitrine de Projetos e Talentos Acadêmicos

## Sobre o Projeto

O **Hub Conecta** é uma aplicação web desenvolvida pelo **DevOps Squad** para a Unidade Curricular de Projeto e Engenharia de Software (Curso de Análise e Desenvolvimento de Sistemas). A plataforma funciona como uma vitrine centralizada, onde squads de estudantes cadastram seus projetos integradores de TI, facilitando a visualização por mentores, avaliadores e empresas parceiras.

---

## Problema que Pretende Resolver

O ambiente acadêmico frequentemente gera projetos incríveis, TCCs e soluções inovadoras que acabam restritos às salas de aula ou a repositórios isolados. O Hub Conecta centraliza a vitrine desses trabalhos, aumentando a visibilidade dos estudantes perante mentores e empresas e promovendo conexões e mentorias direcionadas.

---

## Principais Funcionalidades

- **Vitrine de projetos:** estudantes cadastram projetos (título, descrição e área) e todos os perfis visualizam a vitrine.
- **Meus projetos:** o estudante vê separadamente os projetos que cadastrou.
- **Cadastro e login no servidor:** senhas protegidas com hash BCrypt; o hash nunca é devolvido ao front-end.
- **Controle de acesso por perfil:** telas distintas para Estudante, Mentor e Administrador.
- **Validação de dados no back-end:** campos obrigatórios, e-mail válido, tamanho da senha e perfil permitido, com mensagens de erro claras (HTTP 400).
- **Conta de administrador automática:** criada ao subir o back-end, pois administradores não podem ser cadastrados pelo formulário público.

### Funcionalidades por perfil

| Perfil | O que já funciona |
|---|---|
| Estudante | Cadastra projetos, vê "Meus projetos" e a vitrine |
| Mentor | Visualiza a vitrine de projetos (somente leitura) |
| Administrador | Visualiza a vitrine de projetos (somente leitura) |

> Os cards "Mentorias", "Oportunidades", "Conexões", "Usuários" e similares das áreas logadas, e os exemplos de oportunidades da página inicial, são **ilustrativos** e estão previstos para versões futuras. O perfil **Empresa** também está previsto, mas ainda não possui tela nem cadastro.

---

## Tecnologias Utilizadas

- **Back-end:** Java 17, Spring Boot, Spring Web MVC, Spring Data JPA / Hibernate, Bean Validation
- **Banco de dados:** SQLite
- **Segurança:** `spring-security-crypto` (apenas o `BCryptPasswordEncoder`; o Spring Security completo não é utilizado)
- **Front-end:** HTML5, CSS3 (variáveis CSS, layout responsivo) e JavaScript com requisições `fetch`
- **Versionamento:** Git e GitHub

As dependências do Java são gerenciadas pelo Maven e baixadas automaticamente na primeira execução. O front-end não precisa de instalação de pacotes.

---

## Requisitos para Execução

- **JDK 17** ou superior
- **VS Code** (extensões Java, Spring Boot e **Live Server**) ou IntelliJ IDEA
- **Navegador** atualizado (Chrome, Edge ou Firefox)

Não é necessário instalar o Maven (o projeto usa o Maven Wrapper) nem um SGBD externo.

---

## Como Executar

### 1. Clonar o repositório

```bash
git clone https://github.com/nascorsz/devops-hub.git
cd devops-hub
```

### 2. Executar o back-end

Defina a senha da conta de administrador na variável `ADMIN_SENHA` (de 6 a 72 caracteres) e inicie a aplicação.

**Windows (PowerShell):**

```powershell
cd back-end\backend
$env:ADMIN_SENHA="defina-uma-senha"
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
cd back-end/backend
export ADMIN_SENHA="defina-uma-senha"
./mvnw spring-boot:run
```

O servidor inicia em `http://localhost:8080`. No console devem aparecer `Started BackendApplication` e, na primeira execução, `Administrador criado`.

Também é possível executar pela IDE, rodando a classe `BackendApplication` (`src/main/java/com/squad/backend/BackendApplication.java`). Nesse caso, a variável `ADMIN_SENHA` precisa ser configurada no ambiente de execução da IDE.

### 3. Executar o front-end

1. Abra a pasta `frontend` no VS Code.
2. Clique com o botão direito em `index.html` e escolha **Open with Live Server**.
3. O site abre em `http://127.0.0.1:5500`.

> **Importante:** o front-end deve ser aberto pelo Live Server (porta 5500). O back-end só aceita requisições dessa origem (`127.0.0.1:5500` e `localhost:5500`). Abrir o `index.html` direto do disco causa erro de CORS.

---

## Banco de Dados

O projeto usa **SQLite**, que armazena tudo em um arquivo local. Nenhuma instalação adicional é necessária.

- O arquivo se chama **`banco.db`** (`spring.datasource.url=jdbc:sqlite:banco.db`) e é criado na pasta de onde a aplicação é iniciada, normalmente `back-end/backend/`.
- Ele **não é versionado** (está no `.gitignore`). Na primeira execução, o Hibernate cria as tabelas `usuarios` e `projetos` (`spring.jpa.hibernate.ddl-auto=update`).
- Para recomeçar com um banco vazio, pare a aplicação e apague o `banco.db`. Ele será recriado ao subir novamente, junto com a conta de administrador.

---

## Configuração

As configurações ficam em `back-end/backend/src/main/resources/application.properties`, que define a porta (`8080`), a conexão com o SQLite e a conta de administrador.

| Propriedade | Descrição | Padrão |
|---|---|---|
| `app.admin.nome` | Nome do administrador | `Administrador` |
| `app.admin.email` | E-mail de login do administrador | `admin@hubconecta.com` |
| `app.admin.senha` | Lida da variável de ambiente `ADMIN_SENHA` | (vazio) |

A senha do administrador **não fica no código nem no repositório**. Sem a variável `ADMIN_SENHA`, a conta não é criada e o console exibe um aviso.

---

## Conta de Administrador

Não é possível criar administradores pelo formulário de cadastro: o front-end bloqueia e o servidor recusa (HTTP 400) qualquer perfil diferente de estudante ou mentor.

Ao subir o back-end, a classe `AdminInicializador` cria a conta automaticamente, se ela ainda não existir:

- **E-mail:** `admin@hubconecta.com` (ou o valor de `app.admin.email`)
- **Senha:** a definida em `ADMIN_SENHA`

A senha é gravada somente como hash BCrypt.

---

## API REST

URL base: `http://localhost:8080`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/usuarios/cadastrar` | Cadastra um usuário (perfil `estudante` ou `mentor`) |
| POST | `/api/usuarios/login` | Autentica e retorna os dados do usuário, sem a senha |
| GET | `/api/projetos` | Lista todos os projetos |
| GET | `/api/projetos?autorEmail={email}` | Lista os projetos de um estudante |
| POST | `/api/projetos` | Cadastra um projeto |

### Exemplos de corpo (JSON)

**Cadastro de usuário:**

```json
{ "nome": "Maria", "email": "maria@email.com", "senha": "123456", "tipo": "estudante" }
```

**Login:**

```json
{ "email": "maria@email.com", "senha": "123456" }
```

**Cadastro de projeto** (todos os campos obrigatórios):

```json
{
  "title": "DevOps Hub",
  "description": "Vitrine de projetos acadêmicos",
  "area": "Tecnologia",
  "autor": "Maria",
  "autorEmail": "maria@email.com"
}
```

### Respostas de erro

| Status | Quando acontece | Exemplo de mensagem |
|---|---|---|
| 400 | Dados inválidos ou ausentes | `A área é obrigatória.` / `E-mail em formato inválido.` |
| 400 | Perfil não permitido no cadastro | `Tipo de usuário inválido. Escolha estudante ou mentor.` |
| 400 | E-mail já cadastrado | `Este e-mail já está cadastrado!` |
| 401 | Login com e-mail ou senha incorretos | `E-mail ou senha incorretos!` |

As respostas de erro são enviadas como texto simples.

---

## Como Testar as Principais Funcionalidades

1. **Cadastro:** em `cadastro.html`, informe nome, e-mail, senha (mínimo de 6 caracteres) e perfil (estudante ou mentor).
2. **Login:** em `login.html`, entre com as credenciais. O sistema redireciona para `estudante.html`, `mentor.html` ou `administrador.html`.
3. **Projetos:** como estudante, preencha o formulário "Cadastrar novo projeto". O projeto aparece em "Meus projetos" e na vitrine.
4. **Mentor e administrador:** entre com cada perfil e confira a vitrine somente leitura.
5. **Validações:** tente cadastrar com e-mail inválido, senha curta ou e-mail repetido e veja as mensagens de erro.

---

## Estrutura do Projeto

```text
/
├── back-end/
│   └── backend/                          # Servidor (Spring Boot)
│       ├── pom.xml
│       └── src/main/
│           ├── java/com/squad/backend/
│           │   ├── BackendApplication.java
│           │   ├── config/               # AdminInicializador (cria o administrador)
│           │   ├── controller/           # UsuarioController, ProjetoController
│           │   ├── exception/            # TratadorDeErros (respostas 400)
│           │   ├── model/                # Entidades JPA: Usuario, Projeto
│           │   └── repository/           # Acesso a dados (Spring Data JPA)
│           └── resources/
│               └── application.properties
├── frontend/                             # Interface (HTML, CSS, JS)
│   ├── index.html
│   ├── cadastro.html
│   ├── login.html
│   ├── estudante.html
│   ├── mentor.html
│   ├── administrador.html
│   ├── app.js
│   └── style.css
├── .gitignore
└── README.md
```

---

## Limitações Conhecidas

- **Proteção de telas só no front-end:** o acesso às páginas por perfil é verificado em JavaScript, com o usuário guardado no `sessionStorage`. Alguém com conhecimento técnico consegue contornar essa verificação.
- **Perfil não validado na API de projetos:** o back-end não verifica quem faz a requisição, então qualquer pessoa que chame `POST /api/projetos` diretamente consegue criar um projeto.
- **Autor informado pelo front-end:** os campos `autor` e `autorEmail` vêm do navegador e poderiam ser falsificados em chamadas manuais.
- **Logout no navegador:** o botão "Sair" limpa a sessão do navegador; não há sessão ou token no servidor.
- **CORS restrito ao ambiente local:** apenas `127.0.0.1:5500` e `localhost:5500` são aceitos.
- **Funcionalidades ilustrativas:** mentorias, conexões, gerenciamento de usuários, oportunidades e o perfil Empresa ainda não foram implementados.

## Melhorias Futuras

- Autenticação com JWT e verificação de perfil no back-end, definindo o autor a partir do usuário autenticado.
- Moderação pelo administrador (exclusão e edição de projetos, gerenciamento de usuários).
- Edição e exclusão de projetos pelo próprio estudante.
- Perfil Empresa e funcionalidades de mentoria e conexões entre estudantes, mentores e empresas.
- Testes automatizados.

---

## Equipe DevOps Squad

Integrantes:

- *Maria Eduarda Oliveira*
- *Arthur Sales*
- *João Gabriel Bessa*
- *Lucas Almeida*
- *Paulo Alverga*