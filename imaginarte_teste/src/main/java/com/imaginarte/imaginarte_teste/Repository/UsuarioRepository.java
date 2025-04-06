package com.imaginarte.imaginarte_teste.Repository;

import com.imaginarte.imaginarte_teste.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Usuario findByCpf(String cpf);

    Usuario findByEmail(String email);
}
