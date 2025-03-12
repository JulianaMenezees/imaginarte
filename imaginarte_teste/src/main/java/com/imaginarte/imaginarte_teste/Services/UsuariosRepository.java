package com.imaginarte.imaginarte_teste.Services;

import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuariosRepository extends JpaRepository<UsuarioAdmin, Integer> {
    // Busca os usuários pelo nome, ignorando maiúsculas e minúsculas
    List<UsuarioAdmin> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
