package com.squad.backend.repository;

import com.squad.backend.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    // O Spring Data gera a consulta automaticamente pelo nome do método:
    // busca todos os projetos cujo campo autorEmail seja igual ao informado.
    List<Projeto> findByAutorEmail(String autorEmail);
}