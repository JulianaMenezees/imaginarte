package com.imaginarte.imaginarte_teste.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioRepository, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

}
