// FORMULÁRIO DE CADASTRO (Comunicação com a API)
const formularioCadastro = document.getElementById("formCadastro");

if (formularioCadastro) {
    formularioCadastro.addEventListener("submit", async function(event) {
        event.preventDefault();

        const nome = document.getElementById("nomeCadastro").value;
        const email = document.getElementById("emailCadastro").value;
        const senha = document.getElementById("senhaCadastro").value;
        const tipo = document.getElementById("tipoUsuario").value;

        // REQUISITO DE SEGURANÇA: Impedir cadastro direto como Administrador pelo front-end
        if (tipo === "administrador" || tipo === "admin") {
            alert("Contas administrativas não podem ser criadas por este formulário!");
            return;
        }

        try {
            // Enviando os dados para o Spring Boot salvar no banco SQLite com segurança
            const response = await fetch("http://127.0.0.1:8080/api/usuarios/cadastrar", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ nome, email, senha, tipo })
            });

            if (response.ok) {
                alert("Cadastro realizado com sucesso!");
                window.location.href = "login.html";
            } else {
                const erroMsg = await response.text();
                alert("Erro ao cadastrar: " + erroMsg);
            }
        } catch (error) {
            console.error("Erro de conexão:", error);
            alert("Não foi possível conectar ao servidor. O back-end está rodando?");
        }
    });
}


// FORMULÁRIO DE LOGIN (Autenticação no Servidor)
const formularioLogin = document.getElementById("formLogin");

if (formularioLogin) {
    formularioLogin.addEventListener("submit", async function(event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const senha = document.getElementById("senha").value;

        try {
            // O servidor valida a senha com hash e retorna o usuário se estiver tudo certo
            const response = await fetch("http://127.0.0.1:8080/api/usuarios/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, senha })
            });

            if (response.ok) {
                const usuario = await response.json();
                alert("Bem-vindo ao Hub Conecta, " + usuario.nome + "!");

                // Guardamos apenas a sessão ativa do usuário logado (ou idealmente token no futuro)
                sessionStorage.setItem("usuarioLogado", JSON.stringify(usuario));

                // Redirecionamento por perfil vindo direto do banco de dados do servidor
                if (usuario.tipo === "estudante") {
                    window.location.href = "estudante.html";
                } else if (usuario.tipo === "mentor") {
                    window.location.href = "mentor.html";
                } else if (usuario.tipo === "administrador") {
                    window.location.href = "administrador.html";
                }
            } else {
                alert("E-mail ou senha incorretos!");
            }
        } catch (error) {
            console.error("Erro de conexão:", error);
            alert("Erro ao tentar fazer login. Verifique o servidor.");
        }
    });
}


// MENSAGEM DE BOAS-VINDAS
const mensagem = document.getElementById("mensagemBoasVindas");

if (mensagem) {
    const usuarioLogado = JSON.parse(sessionStorage.getItem("usuarioLogado"));

    if (usuarioLogado) {
        mensagem.textContent = "Olá, " + usuarioLogado.nome + "!";
    }
}


// FUNÇÃO SAIR (LOGOUT REAL)
// Vincule esta função a um botão de Logout no seu HTML (ex: onclick="sair()")
function sair() {
    sessionStorage.removeItem("usuarioLogado");
    // Se o back-end tiver controle de sessão HTTP, faríamos um fetch para /api/auth/logout aqui
    window.location.href = "index.html";
}


// PROTEÇÃO DAS ÁREAS POR PERFIL
const perfilNecessario = document.body.dataset.perfil;

if (perfilNecessario) {
    const usuarioLogado = JSON.parse(sessionStorage.getItem("usuarioLogado"));

    if (!usuarioLogado || usuarioLogado.tipo !== perfilNecessario) {
        alert("Você não tem autorização para acessar esta página!");
        window.location.href = "login.html";
    }
}


// PROJETOS (VITRINE)
const API_PROJETOS = "http://127.0.0.1:8080/api/projetos";

// Monta o card visual de um projeto.
// Usa textContent (e não innerHTML) de propósito: como título e descrição são digitados
// por usuários, isso impede que alguém injete HTML/JavaScript na página (ataque XSS).
function criarCardProjeto(projeto) {
    const card = document.createElement("div");
    card.className = "card projeto-card";

    const titulo = document.createElement("h3");
    titulo.textContent = projeto.title;

    const area = document.createElement("span");
    area.className = "projeto-area";
    area.textContent = projeto.area;

    const descricao = document.createElement("p");
    descricao.textContent = projeto.description;

    const autor = document.createElement("small");
    autor.textContent = "Por " + projeto.autor;

    card.append(titulo, area, descricao, autor);
    return card;
}

// Busca os projetos na API e desenha os cards dentro do elemento com o id informado.
// Sem autorEmail: lista todos (vitrine). Com autorEmail: lista só os "meus projetos".
async function carregarProjetos(idContainer, autorEmail) {
    const container = document.getElementById(idContainer);
    if (!container) return; // a página atual não tem essa lista

    let url = API_PROJETOS;
    if (autorEmail) {
        url += "?autorEmail=" + encodeURIComponent(autorEmail);
    }

    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error("Falha ao listar projetos");

        const projetos = await response.json();
        container.replaceChildren(); // limpa o conteúdo anterior

        if (projetos.length === 0) {
            container.textContent = "Nenhum projeto cadastrado ainda.";
            return;
        }

        projetos.forEach(function(projeto) {
            container.appendChild(criarCardProjeto(projeto));
        });
    } catch (error) {
        console.error("Erro ao carregar projetos:", error);
        container.textContent = "Não foi possível carregar os projetos. O back-end está rodando?";
    }
}

// FORMULÁRIO DE NOVO PROJETO (apenas na página do estudante)
const formularioProjeto = document.getElementById("formProjeto");

if (formularioProjeto) {
    formularioProjeto.addEventListener("submit", async function(event) {
        event.preventDefault();

        // O autor vem da sessão do usuário logado, não de um campo digitado
        const usuarioLogado = JSON.parse(sessionStorage.getItem("usuarioLogado"));
        if (!usuarioLogado) {
            alert("Sessão expirada. Faça login novamente.");
            window.location.href = "login.html";
            return;
        }

        const projeto = {
            title: document.getElementById("tituloProjeto").value,
            description: document.getElementById("descricaoProjeto").value,
            area: document.getElementById("areaProjeto").value,
            autor: usuarioLogado.nome,
            autorEmail: usuarioLogado.email
        };

        try {
            const response = await fetch(API_PROJETOS, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(projeto)
            });

            if (response.ok) {
                alert("Projeto cadastrado com sucesso!");
                formularioProjeto.reset();
                // Atualiza as duas listas da tela sem recarregar a página
                carregarProjetos("listaMeusProjetos", usuarioLogado.email);
                carregarProjetos("listaVitrine");
            } else {
                // Mostra a mensagem de validação enviada pelo TratadorDeErros
                const erroMsg = await response.text();
                alert("Erro ao cadastrar projeto: " + erroMsg);
            }
        } catch (error) {
            console.error("Erro de conexão:", error);
            alert("Não foi possível conectar ao servidor. O back-end está rodando?");
        }
    });
}

// CARREGAMENTO INICIAL DAS LISTAS
// Cada página só tem os elementos que lhe interessam; a função ignora o que não existir.
const usuarioDaSessao = JSON.parse(sessionStorage.getItem("usuarioLogado"));

if (usuarioDaSessao) {
    carregarProjetos("listaVitrine");                                  // todas as páginas logadas
    carregarProjetos("listaMeusProjetos", usuarioDaSessao.email);      // só existe no estudante.html
}