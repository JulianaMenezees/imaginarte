package com.imaginarte.imaginarte_teste.service;

import com.imaginarte.imaginarte_teste.Repository.PedidoRepository;
import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Método para buscar os pedidos de um usuário
    public List<Pedido> buscarPedidosPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }

    // Método para buscar um pedido específico
    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}

