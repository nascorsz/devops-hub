package com.squad.backend.repository;

import com.squad.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método auxiliar para buscar um usuário pelo e-mail (usado no login e validação de duplicidade)
    Optional<Usuario> findByEmail(String email);
}