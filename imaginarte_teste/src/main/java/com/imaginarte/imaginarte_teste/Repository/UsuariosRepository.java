package com.imaginarte.imaginarte_teste.Repository;

import com.imaginarte.imaginarte_teste.model.UsuarioAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuariosRepository extends JpaRepository<UsuarioAdmin, Integer> {
    // Busca os usuários pelo nome, ignorando maiúsculas e minúsculas
    List<UsuarioAdmin> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    @Query(value = "select * from imaginarte.usuario_admin where email = :email and senha = :senha", nativeQuery = true)
    public UsuarioAdmin login(String email, String senha);

    UsuarioAdmin findByCpf(String cpf);
}
