package com.imaginarte.imaginarte_teste.Repository;

import com.imaginarte.imaginarte_teste.model.EnderecoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoEntregaRepository extends JpaRepository<EnderecoEntrega, Long> {
    List<EnderecoEntrega> findByUsuarioId(int usuarioId);
}

