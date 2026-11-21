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

    // GET /api/projetos                       -> lista TODOS os projetos (vitrine, mentor, admin)
    // GET /api/projetos?autorEmail=a@b.com    -> lista só os projetos desse estudante ("meus projetos")
    @GetMapping
    public List<Projeto> listarProjetos(@RequestParam(required = false) String autorEmail) {
        if (autorEmail == null || autorEmail.isBlank()) {
            return projetoRepository.findAll();
        }
        return projetoRepository.findByAutorEmail(autorEmail);
    }

    // POST /api/projetos -> salva um novo projeto
    // O @Valid checa as regras da classe Projeto (título, descrição, área e autor obrigatórios).
    // Se algo estiver inválido, o TratadorDeErros devolve 400 com a mensagem.
    @PostMapping
    public Projeto criarProjeto(@Valid @RequestBody Projeto projeto) {
        return projetoRepository.save(projeto);
    }
}