package com.squad.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
// Nome da tabela no SQLite (antes era "opportunities"; agora reflete o foco: vitrine de projetos)
@Table(name = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Obrigatório e com no máximo 150 caracteres
    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    @Column(nullable = false)
    private String title;

    // Obrigatória e com no máximo 1000 caracteres
    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
    @Column(nullable = false)
    private String description;

    // Obrigatória (ex.: Tecnologia, Saúde, Educação)
    @NotBlank(message = "A área é obrigatória.")
    @Column(nullable = false)
    private String area;

    // Nome do estudante que cadastrou o projeto (exibido na vitrine)
    @NotBlank(message = "O autor é obrigatório.")
    @Column(nullable = false)
    private String autor;

    // E-mail do estudante autor. Usado para filtrar "meus projetos",
    // porque o e-mail é único e o nome pode se repetir.
    @NotBlank(message = "O e-mail do autor é obrigatório.")
    @Email(message = "E-mail do autor em formato inválido.")
    @Column(nullable = false)
    private String autorEmail;

    // Construtor padrão obrigatório pelo JPA
    public Projeto() {}

    // Construtor com parâmetros
    public Projeto(String title, String description, String area, String autor, String autorEmail) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.autor = autor;
        this.autorEmail = autorEmail;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getAutorEmail() { return autorEmail; }
    public void setAutorEmail(String autorEmail) { this.autorEmail = autorEmail; }
}