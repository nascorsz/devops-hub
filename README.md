# 🚀 DevOps Hub — Vitrine de Projetos e Talentos Acadêmicos

Plataforma desenvolvida para a Unidade Curricular de **Projeto e Engenharia de Software (A3)**, conectando squads acadêmicos, projetos, mentores e oportunidades.

## 🛠️ Tecnologias Utilizadas
* **Back-end:** Java 17+, Spring Boot 3, Spring Web, Spring Data JPA
* **Banco de Dados:** SQLite
* **Front-end:** HTML5, CSS3, JavaScript (Vanilla)
* **Versionamento:** Git & GitHub

---

## ⚙️ Como Executar o Back-end Localmente

1. Navegue até a pasta `backend/` e execute a classe principal `BackendApplication.java` (ou rode `./mvnw spring-boot:run` no terminal).
2. A aplicação iniciará na porta **`8080`** e criará automaticamente o arquivo de banco de dados local **`banco.db`**.

---

## 📡 Endpoints da API

* **Listar Projetos (GET):** `http://localhost:8080/api/projetos`
* **Cadastrar Projeto (POST):** `http://localhost:8080/api/projetos`
  * *Exemplo de corpo JSON para teste (via Postman ou Insomnia):*
    ```json
    {
      "title": "Sistema de Gestão Acadêmica",
      "description": "Plataforma web para controle de notas e frequência.",
      "area": "Tecnologia"
    }
    ```

  