package com.squad.backend.controller;

import com.squad.backend.model.Usuario;
import com.squad.backend.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// Permite que o front-end (Live Server) acesse esta API sem bloqueio de CORS
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Injeção por construtor: o Spring entrega o repository pronto para uso
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Rota de Cadastro (POST /api/usuarios/cadastrar)
    // O @Valid faz o Spring checar as regras da classe Usuario (nome, e-mail, senha)
    // antes de executar o método. Se algo estiver inválido, devolve 400 automaticamente.
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
        // Segurança: esta rota pública só aceita os perfis "estudante" e "mentor".
        // Qualquer outro valor (inclusive "administrador" ou "empresa") é recusado com 400,
        // mesmo que alguém chame a API diretamente, sem passar pelo formulário.
        String tipo = usuario.getTipo() == null ? "" : usuario.getTipo().trim().toLowerCase();
        if (!tipo.equals("estudante") && !tipo.equals("mentor")) {
            return ResponseEntity.badRequest().body("Tipo de usuário inválido. Escolha estudante ou mentor.");
        }
        usuario.setTipo(tipo);

        // Impede dois cadastros com o mesmo e-mail
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.badRequest().body("Este e-mail já está cadastrado!");
        }

        // Segurança: salva apenas o hash BCrypt, nunca a senha em texto puro
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        Usuario novoUsuario = usuarioRepository.save(usuario);
        novoUsuario.setSenha(null); // não devolve o hash da senha ao front-end
        return ResponseEntity.ok(novoUsuario);
    }

    // Rota de Login (POST /api/usuarios/login)
    // ATENÇÃO: sem @Valid aqui de propósito. O login só envia e-mail e senha,
    // e a validação exigiria também o nome.
    @PostMapping("/login")
    public ResponseEntity<?> realizarLogin(@RequestBody Usuario loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Compara a senha digitada com o hash salvo no banco
            if (passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                usuario.setSenha(null); // remove a senha antes de enviar ao front-end
                return ResponseEntity.ok(usuario);
            }
        }

        // Mensagem genérica: não revela se o erro foi no e-mail ou na senha
        return ResponseEntity.status(401).body("E-mail ou senha incorretos!");
    }
}