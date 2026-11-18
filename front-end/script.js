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
            const response = await fetch("http://localhost:8080/api/usuarios/cadastrar", {
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
            const response = await fetch("http://localhost:8080/api/usuarios/login", {
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