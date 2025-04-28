package com.imaginarte.imaginarte_teste.service;

import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import com.imaginarte.imaginarte_teste.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Este método você precisa criar
    public List<Pedido> buscarPedidosPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }
}
