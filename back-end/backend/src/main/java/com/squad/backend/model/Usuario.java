package com.squad.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Não pode ser nulo nem vazio (inclui só espaços)
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    // Obrigatório, precisa ter formato de e-mail válido e ser único no banco
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail em formato inválido.")
    @Column(unique = true)
    private String email;

    // Obrigatório, de 6 a 72 caracteres. O limite de 72 existe porque o hash BCrypt
    // (60 caracteres) também passa por esta validação ao salvar no banco.
    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, max = 72, message = "A senha deve ter entre 6 e 72 caracteres.")
    private String senha; // Armazenará a senha criptografada (Hash)

    private String tipo;   // "estudante", "mentor", "administrador"

    // Construtor vazio (obrigatório para o JPA/Hibernate)
    public Usuario() {
    }

    // Construtor com campos
    public Usuario(String nome, String email, String senha, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}