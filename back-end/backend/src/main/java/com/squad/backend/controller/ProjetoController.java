package com.squad.backend.controller;

import com.squad.backend.model.Projeto;
import com.squad.backend.repository.ProjetoRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
// Permite que o front-end (Live Server) acesse esta API sem bloqueio de CORS
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class ProjetoController {

    private final ProjetoRepository projetoRepository;

    // Injeção por construtor: o Spring entrega o repository pronto para uso
    public ProjetoController(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    // GET /api/projetos -> lista todos os projetos salvos no SQLite
    @GetMapping
    public List<Projeto> listarProjetos() {
        return projetoRepository.findAll();
    }

    // POST /api/projetos -> salva um novo projeto
    // O @Valid checa as regras da classe Projeto (título, descrição e área obrigatórios).
    // Se algo estiver inválido, devolve 400 com a mensagem do erro.
    @PostMapping
    public Projeto criarProjeto(@Valid @RequestBody Projeto projeto) {
        return projetoRepository.save(projeto);
    }
}