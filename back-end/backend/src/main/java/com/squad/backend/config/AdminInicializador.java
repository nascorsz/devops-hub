package com.squad.backend.config;

import com.squad.backend.model.Usuario;
import com.squad.backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// O CommandLineRunner executa o método run() uma vez, logo depois que a aplicação sobe.
// Aqui ele serve para garantir que exista uma conta de administrador no banco.
@Component
public class AdminInicializador implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicializador.class);

    private final UsuarioRepository usuarioRepository;
    private final String nome;
    private final String email;
    private final String senha;

    // Os valores vêm do application.properties (que por sua vez lê a senha de uma variável de ambiente)
    public AdminInicializador(UsuarioRepository usuarioRepository,
                              @Value("${app.admin.nome:Administrador}") String nome,
                              @Value("${app.admin.email:admin@hubconecta.com}") String email,
                              @Value("${app.admin.senha:}") String senha) {
        this.usuarioRepository = usuarioRepository;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public void run(String... args) {
        // Sem senha definida, não cria o admin (evita uma conta com senha conhecida ou vazia)
        if (senha == null || senha.isBlank()) {
            log.warn("Administrador NÃO criado: defina a variável de ambiente ADMIN_SENHA.");
            return;
        }

        // Mesma regra de tamanho da validação do Usuario (6 a 72 caracteres)
        if (senha.length() < 6 || senha.length() > 72) {
            log.warn("Administrador NÃO criado: a senha deve ter entre 6 e 72 caracteres.");
            return;
        }

        // Se o admin já existe (de uma execução anterior), não cria de novo
        if (usuarioRepository.findByEmail(email).isPresent()) {
            log.info("Administrador já existe: {}", email);
            return;
        }

        // Salva apenas o hash BCrypt, nunca a senha em texto puro
        String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
        usuarioRepository.save(new Usuario(nome, email, senhaCriptografada, "administrador"));

        log.info("Administrador criado: {}", email);
    }
}