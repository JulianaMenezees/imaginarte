package com.imaginarte.imaginarte_teste.Repository;

import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
        List<Pedido> findByUsuario(Usuario usuario);
}
