## DevOps Hub - Vitrine de Projetos e Talentos Acadêmicos

# Sobre o Projeto
O **DevOps Hub** é uma aplicação web desenvolvida para a Unidade Curricular de Projeto e Engenharia de Software (Curso de Análise e Desenvolvimento de Sistemas). A plataforma atua como uma vitrine centralizada onde squads de estudantes cadastram seus projetos integradores de TI, facilitando a visualização por mentores, avaliadores e empresas parceiras.

---

## Problema que Pretesnde Resolver
O ambiente acadêmico frequentemente gera projetos incríveis, TCCs e soluções inovadoras que acabam ficando restritos às salas de aula ou a repositórios isolados. O **DevOps Hub** resolve esse problema ao centralizar a vitrine desses trabalhos, facilitando a visibilidade dos estudantes perante mentores e empresas, além de promover conexões estratégicas e mentorias direcionadas.

---

## Principais Funcionalidades
* **Vitrine de Projetos:** Listagem pública e cadastro de projetos desenvolvidos por acadêmicos.
* **Autenticação Segura no Servidor:** Sistema robusto de login e cadastro com senhas protegidas por hash (BCrypt).
* **Controle de Acesso por Perfis:** Distinção de telas e permissões para Estudantes, Mentores e Administradores.
* **Gerenciamento de Sessão:** Função de saída (*Logout*) real que encerra o contexto do usuário com segurança.
* **Persistência Real:** Armazenamento centralizado em banco de dados relacional, substituindo o uso obsoleto de `localStorage` para dados sensíveis.

---

## Tecnologias Utilizadas
* **Back-end:** Java 17, Spring Boot, Spring Data JPA / Hibernate
* **Banco de Dados:** SQLite
* **Segurança:** Spring Security / BCrypt Password Encoder
* **Front-end:** HTML5, CSS3, JavaScript (com requisições assíncronas via `fetch`)
* **Versionamento:** Git e GitHub (com commits semânticos por marcos funcionais)

---

## Requisitos Necessários para Execução
* **Java Development Kit (JDK):** Versão 17 ou superior instalada na máquina[cite: 1].
* **Editor de Código:** VS Code (com as extensões de Java, Spring Boot e Live Server recomendadas) ou IntelliJ IDEA.
* **Navegador Web:** Google Chrome, Edge ou Firefox atualizados.

---

## Como Baixar ou Clonar o Projeto

### 1. Clonar o Repositório
Abra o seu terminal e execute o comando abaixo para clonar o repositório oficial:

```bash
git clone https://github.com/nascorsz/devops-hub.git
cd devops-hub
```

### 2. Executar o Back-end (Spring Boot + SQLite)
Navegue até a pasta do back-end e execute o projeto:

```bash
cd backend
./mvnw spring-boot:run
```
> O servidor iniciará na porta `8080` (http://localhost:8080) e o banco de dados `database.db` será criado automaticamente na primeira execução.

### 3. Executar o Front-end
1. Navegue até a pasta `frontend`.
2. Abra o arquivo `index.html` em qualquer navegador web (ou utilize a extensão *Live Server* no VS Code).

---

## Como Instalar as Dependências
O projeto utiliza o Maven para o gerenciamento de dependências do Java. As bibliotecas do Spring Boot, JPA, SQLite e BCrypt serão baixadas automaticamente ao abrir o projeto ou ao executar o build. Não é necessária a instalação manual de pacotes no front-end.

---

## Como Configurar e Estruturar o Banco de Dados
O projeto utiliza o banco de dados SQLite, que opera através de um arquivo local gerenciado automaticamente pelo Hibernate/JPA

### 1. Nenhuma instalação de SGBD externo (como MySQL ou PostgreSQL) é necessária.

### 2. A estrutura das tabelas (usuarios, projetos, etc.) é criada automaticamente pelo Spring Boot na primeira execução da aplicação através da configuração de persistência.

--- 

## Como Configurar Variáveis de Ambiente
Atualmente, as configurações da aplicação encontram-se centralizadas no arquivo `application.properties` localizado em:

```text
back-end/backend/
├── pom.xml
└── src/
    └── main/
        └── resources/
            └── application.properties
```
        
 Contendo a definição padrão da porta e a conexão com o SQLite. Caso precise alterar portas ou parâmetros de conexão, ajuste-os diretamente neste arquivo.

 ---

 ## Como Executar/Testar a Aplicação

### 1. Executando o Back-end (API REST)
Abra a pasta backend/ no seu VS Code.

Navegue até o arquivo principal: src/main/java/com/squad/backend/BackendApplication.java.

Clique no botão Run (ou execute via terminal com ./mvnw spring-boot:run).

O servidor iniciará na porta padrão 8080 e gerará o arquivo banco.db na raiz do back-end.

### 2. Executando o Front-end
Abra a pasta frontend/ no VS Code.

Utilize a extensão Live Server ou abra o arquivo index.html diretamente no seu navegador.

---

## Como Testar as Principais Funcionalidades

* **Cadastro de Novo Usuário:** Acesse `cadastro.html`, preencha seus dados informando **nome**, **e-mail**, **senha** e o **perfil desejado**. O sistema criptografará sua senha via BCrypt no servidor.

**Login Seguro:** Acesse `login.html`, entre com o **e-mail** e **senha** cadastrados. O servidor validará a credencial e redirecionará para a página correta (estudante.html, mentor.html ou administrador.html).

**Consumo da API de Projetos:** Utilize a interface de projetos para cadastrar novas iniciativas (via POST /api/projetos) e listá-las dinamicamente (via GET /api/projetos).

## Credenciais de Teste
Você pode registrar uma nova conta diretamente pelo formulário de cadastro da aplicação. Contas administrativas possuem restrição de criação por formulário público por motivos de segurança, devendo ser inseridas diretamente para validação interna do sistema se necessário.

## Estrutura Geral do Projeto

```text
/
├── backend/                          # Código-fonte do Servidor (Spring Boot)
│   ├── src/main/java/com/squad/backend/
│   │   ├── controller/               # Controladores REST (ProjetoController, UsuarioController)
│   │   ├── model/                    # Entidades JPA (Projeto, Usuario)
│   │   └── repository/               # Interfaces de Acesso a Dados (Spring Data JPA)
│   └── src/main/resources/           # Configurações (application.properties)
├── frontend/                         # Interface Gráfica (HTML, CSS, JS)
│   ├── index.html
│   ├── cadastro.html
│   ├── login.html
│   ├── estudante.html
│   ├── mentor.html
│   ├── administrador.html
│   ├── app.js
│   └── style.css
└── README.md                         # Documentação oficial do projeto
```

---

## Equipe DevOps Squad