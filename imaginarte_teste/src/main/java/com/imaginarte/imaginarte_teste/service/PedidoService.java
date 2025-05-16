package com.imaginarte.imaginarte_teste.service;

import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.StatusPedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import com.imaginarte.imaginarte_teste.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> buscarPedidosPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);

    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarTodosOrdenadosPorData() {
        return pedidoRepository.findAllByOrderByDataPedidoDesc();
    }

    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}
