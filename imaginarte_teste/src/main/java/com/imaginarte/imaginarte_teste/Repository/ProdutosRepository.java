package com.imaginarte.imaginarte_teste.Repository;

import com.imaginarte.imaginarte_teste.model.ProdutoAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<ProdutoAdmin, Integer> {

    List<ProdutoAdmin> findByNomeContainingIgnoreCase(String nome);

    List<ProdutoAdmin> findBySituacaoTrue();
}
