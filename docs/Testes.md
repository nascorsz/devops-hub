# Plano e Registro de Testes - Hub Conecta

## 1. Visão geral

Os testes do Hub Conecta foram **manuais e funcionais**, executados pela equipe com o sistema rodando localmente. Não há testes automatizados nesta versão (ver "Melhorias futuras" no README).

| Item | Descrição |
|---|---|
| **Período** | Novembro/2026 |
| **Back-end** | Spring Boot em `http://localhost:8080` (`mvnw spring-boot:run`) |
| **Front-end** | Live Server em `http://127.0.0.1:5500` |
| **Banco** | SQLite (`banco.db`), criado automaticamente |
| **Navegador** | Google Chrome, com DevTools (Console e aba Network) |
| **Responsável pela execução** | _preencher_ |

### Técnicas utilizadas

- **Teste pela interface:** formulários e telas usados como um usuário real.
- **Teste de API pelo Console do navegador:** requisições `fetch` enviadas diretamente às rotas, para exercitar casos que a interface bloqueia (por exemplo, perfil inválido).
- **Inspeção da aba Network:** conferência de status HTTP, preflight (`OPTIONS`) e cabeçalhos de CORS.
- **Teste de clone limpo:** projeto clonado em uma pasta nova e executado seguindo apenas o README.

### Pré-condições

- Backend iniciado com a variável `ADMIN_SENHA` definida.
- Front-end aberto pelo Live Server (origem `127.0.0.1:5500`).
- E-mails de teste ainda não cadastrados.

---

## 2. Casos de teste

**Legenda:** RF = requisito funcional, RNF = requisito não funcional. Todos os casos abaixo foram executados e **aprovados**.

### 2.1 Infraestrutura e integração

| ID | Caso de teste | Passos | Resultado esperado | Resultado | Status |
|---|---|---|---|---|---|
| CT-01 | Inicialização do back-end | Rodar `mvnw spring-boot:run` | Console exibe `Started BackendApplication` na porta 8080, sem erros | Conforme esperado | Aprovado |
| CT-02 | Criação automática do banco | Subir o back-end sem `banco.db` | Arquivo `banco.db` criado e tabelas `usuarios` e `projetos` geradas | Conforme esperado | Aprovado |
| CT-03 | CORS com a origem permitida | Abrir o site pelo Live Server e enviar um cadastro | Preflight `OPTIONS` retorna 200 com cabeçalhos CORS e o `POST` é concluído, sem erro no Console | Conforme esperado | Aprovado |
| CT-04 | Listagem de projetos pela API | Abrir `http://localhost:8080/api/projetos` | Retorna um array JSON (vazio ou com projetos) | Conforme esperado | Aprovado |

### 2.2 Cadastro (RF: cadastrar usuário)

| ID | Caso de teste | Passos | Resultado esperado | Resultado | Status |
|---|---|---|---|---|---|
| CT-05 | Cadastro de estudante | Em `cadastro.html`, preencher dados válidos e perfil Estudante | Alerta "Cadastro realizado com sucesso!" e redirecionamento ao login | Conforme esperado | Aprovado |
| CT-06 | Cadastro de mentor | Repetir com perfil Mentor e outro e-mail | Cadastro realizado com sucesso | Conforme esperado | Aprovado |
| CT-07 | Senha armazenada com hash | Conferir a tabela `usuarios` no banco | Coluna `senha` com hash BCrypt (início `$2a$`), nunca a senha em texto puro | Conforme esperado | Aprovado |
| CT-08 | Senha não devolvida na resposta | Na aba Network, abrir a resposta de `cadastrar` | Campo `senha` vem como `null` | Conforme esperado | Aprovado |
| CT-09 | E-mail já cadastrado | Cadastrar novamente o mesmo e-mail | Mensagem "Este e-mail já está cadastrado!" (HTTP 400) | Conforme esperado | Aprovado |
| CT-10 | E-mail em formato inválido (API) | `fetch` de cadastro com e-mail `abc` | HTTP 400 com "E-mail em formato inválido." | Conforme esperado | Aprovado |
| CT-11 | Senha curta | Tentar senha com menos de 6 caracteres | Bloqueado pelo formulário; na API, HTTP 400 com mensagem de tamanho | Conforme esperado | Aprovado |
| CT-12 | Nome vazio (API) | `fetch` de cadastro sem nome | HTTP 400 com "O nome é obrigatório." | Conforme esperado | Aprovado |
| CT-13 | Perfil não permitido (API) | `fetch` de cadastro com `tipo: "empresa"` (ou `administrador`) | HTTP 400 com "Tipo de usuário inválido. Escolha estudante ou mentor." | Conforme esperado | Aprovado |
| CT-14 | Opções de perfil no formulário | Abrir o select de perfil | Somente Estudante e Mentor | Conforme esperado | Aprovado |

### 2.3 Autenticação e controle de acesso (RF: login e perfis)

| ID | Caso de teste | Passos | Resultado esperado | Resultado | Status |
|---|---|---|---|---|---|
| CT-15 | Login de estudante | Entrar com as credenciais do estudante | Abre `estudante.html` com "Olá, Estudante Teste!" | Conforme esperado | Aprovado |
| CT-16 | Login de mentor | Entrar com as credenciais do mentor | Abre `mentor.html`, sem formulário de projeto | Conforme esperado | Aprovado |
| CT-17 | Login de administrador | Entrar com `admin@hubconecta.com` e a senha de `ADMIN_SENHA` | Abre `administrador.html` | Conforme esperado | Aprovado |
| CT-18 | Login com senha errada | Informar senha incorreta | Alerta "E-mail ou senha incorretos!" (HTTP 401) | Conforme esperado | Aprovado |
| CT-19 | Acesso sem login | Digitar `estudante.html` na barra do navegador sem estar logado | Alerta de falta de autorização e retorno ao login | Conforme esperado | Aprovado |
| CT-20 | Acesso a tela de outro perfil | Logado como estudante, abrir `administrador.html` | Alerta de falta de autorização | Conforme esperado | Aprovado |
| CT-21 | Logout | Clicar em "Sair" | Sessão removida e retorno ao `index.html` | Conforme esperado | Aprovado |
| CT-22 | Criação do administrador | Subir o back-end com `ADMIN_SENHA` definida | Console exibe `Administrador criado` | Conforme esperado | Aprovado |
| CT-23 | Administrador sem duplicidade | Reiniciar o back-end | Console exibe `Administrador já existe`, sem erro | Conforme esperado | Aprovado |

### 2.4 Projetos (RF: vitrine de projetos)

| ID | Caso de teste | Passos | Resultado esperado | Resultado | Status |
|---|---|---|---|---|---|
| CT-24 | Cadastro de projeto | Como estudante, preencher o formulário e enviar | Alerta de sucesso; projeto aparece em "Meus projetos" e na vitrine sem recarregar a página | Conforme esperado | Aprovado |
| CT-25 | Projeto sem campo obrigatório (API) | `fetch` de `POST /api/projetos` sem `area` | HTTP 400 com "A área é obrigatória." | Conforme esperado | Aprovado |
| CT-26 | Projeto sem autor (API) | `fetch` sem `autor` e `autorEmail` | HTTP 400 com as mensagens do autor | Conforme esperado | Aprovado |
| CT-27 | Filtro "Meus projetos" | Abrir `/api/projetos?autorEmail=<email do estudante>` | Retorna só os projetos desse estudante; outro e-mail retorna `[]` | Conforme esperado | Aprovado |
| CT-28 | Vitrine para mentor | Logado como mentor, abrir a tela | Lista os projetos dos estudantes, somente leitura | Conforme esperado | Aprovado |
| CT-29 | Vitrine para administrador | Logado como admin, abrir a tela | Lista os projetos, somente leitura | Conforme esperado | Aprovado |
| CT-30 | Persistência | Reiniciar o back-end e recarregar a vitrine | Projetos continuam listados (dados gravados no SQLite) | Conforme esperado | Aprovado |

### 2.5 Interface, documentação e entrega

| ID | Caso de teste | Passos | Resultado esperado | Resultado | Status |
|---|---|---|---|---|---|
| CT-31 | Navegação da página inicial | Percorrer menu, botões e links da `index.html` | Todos levam a uma página ou seção válida | Conforme esperado | Aprovado |
| CT-32 | Layout responsivo | Reduzir a janela (F12, modo celular) | Cards e formulários se reorganizam sem rolagem horizontal | Conforme esperado | Aprovado |
| CT-33 | Navegação por teclado | Percorrer formulários com `Tab` | Campo em foco sempre visível | Conforme esperado | Aprovado |
| CT-34 | Clone limpo pelo README | Clonar o repositório em pasta nova e seguir apenas o README | Back-end sobe, admin é criado, cadastro, login e vitrine funcionam | Conforme esperado | Aprovado |
| CT-35 | Repositório sem arquivos indevidos | Conferir `git ls-files` e o GitHub | Sem `target/`, `.db`, logs ou `.vscode/` versionados | Conforme esperado | Aprovado |

---

## 3. Resumo

| Área | Casos | Aprovados | Reprovados |
|---|---|---|---|
| Infraestrutura e integração | 4 | 4 | 0 |
| Cadastro | 10 | 10 | 0 |
| Autenticação e acesso | 9 | 9 | 0 |
| Projetos | 7 | 7 | 0 |
| Interface, documentação e entrega | 5 | 5 | 0 |
| **Total** | **35** | **35** | **0** |

## 4. Problemas encontrados durante os testes (e como foram resolvidos)

| Problema | Causa | Solução |
|---|---|---|
| Erro de CORS no cadastro, mesmo com `@CrossOrigin` | Arquivos com extensão `.Java` (maiúscula) não eram compilados pelo Maven, então o `UsuarioController` nem existia na aplicação em execução | Renomear para `.java`, limpar o build e adicionar a dependência `spring-security-crypto` |
| Preflight com status 200 mas sem cabeçalhos CORS | Rota inexistente respondida pelo handler padrão do Spring | Resolvido junto com o problema anterior |
| Cadastro devolvia o hash da senha | Objeto salvo era retornado inteiro | `setSenha(null)` antes de responder |
| Perfil "Empresa" aceito no cadastro, sem tela correspondente | Falta de validação do campo `tipo` | Opção removida do formulário e validação no back-end (HTTP 400) |
| Logs da JVM (`hs_err_pid*.log`) versionados | `.gitignore` criado na pasta errada | `git rm --cached` e `.gitignore` centralizado na raiz |

## 5. Limitações dos testes

- Todos os testes foram manuais, sem automação.
- Não foram feitos testes de carga, de desempenho nem de segurança aprofundados (por exemplo, ataques a um servidor publicado).
- O teste de CORS cobre apenas as origens locais liberadas (`127.0.0.1:5500` e `localhost:5500`).

## 6. Melhorias futuras de teste

- Testes unitários dos controllers com JUnit e Mockito.
- Testes de integração da API com `MockMvc` e banco em memória.
- Testes ponta a ponta do front-end (por exemplo, com Playwright ou Cypress).
- Verificação automática no GitHub Actions a cada commit.
