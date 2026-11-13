package com.squad.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "opportunities")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String area;

    // Construtor padrão obrigatório pelo JPA
    public Projeto() {}

    // Construtor com parâmetros
    public Projeto(String title, String description, String area) {
        this.title = title;
        this.description = description;
        this.area = area;
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
}
