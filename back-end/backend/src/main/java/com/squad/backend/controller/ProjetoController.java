package com.squad.backend.controller;

import com.squad.backend.model.Projeto;
import com.squad.backend.repository.ProjetoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*") // Libera o acesso para o front-end (HTML/JS) sem bloqueio de navegador
public class ProjetoController {

    private final ProjetoRepository projetoRepository;

    // Injeção por construtor (Recomendação moderna do Spring Boot)
    public ProjetoController(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    // Rota GET (http://localhost:8080/api/projetos) -> Lista todos os projetos do SQLite
    @GetMapping
    public List<Projeto> listarProjetos() {
        return projetoRepository.findAll();
    }

    // Rota POST (http://localhost:8080/api/projetos) -> Salva um novo projeto no SQLite
    @PostMapping
    public Projeto criarProjeto(@RequestBody Projeto projeto) {
        return projetoRepository.save(projeto);
    }
}