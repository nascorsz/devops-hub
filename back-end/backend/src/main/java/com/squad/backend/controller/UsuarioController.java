package com.squad.backend.controller;

import com.squad.backend.model.Usuario;
import com.squad.backend.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Injeção por construtor (seguindo as boas práticas que adotamos)
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Rota de Cadastro (/api/usuarios/cadastrar)
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        // Verifica se já existe um usuário com este e-mail
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.badRequest().body("Este e-mail já está cadastrado!");
        }

        // REQUISITO DE SEGURANÇA: Criptografar a senha com Hash (BCrypt) antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Garante por segurança que ninguém consiga se cadastrar como admin por este endpoint público
        if (usuario.getTipo() == null || usuario.getTipo().equalsIgnoreCase("administrador") || usuario.getTipo().equalsIgnoreCase("admin")) {
            usuario.setTipo("estudante");
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Rota de Login (/api/usuarios/login)
    @PostMapping("/login")
    public ResponseEntity<?> realizarLogin(@RequestBody Usuario loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Compara a senha digitada com o Hash salvo no banco SQLite
            if (passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                // Remove a senha do objeto retornado por segurança antes de enviar ao front-end
                usuario.setSenha(null);
                return ResponseEntity.ok(usuario);
            }
        }

        return ResponseEntity.status(401).body("E-mail ou senha incorretos!");
    }
}