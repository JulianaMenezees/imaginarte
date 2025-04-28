package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import com.imaginarte.imaginarte_teste.service.PedidoService;
import com.imaginarte.imaginarte_teste.service.UsuarioService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class PedidoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/usuario/pedidos")
    public String listarPedidos(Model model) {
        try {
            Usuario usuario = usuarioService.getUsuarioLogado();
            List<Pedido> pedidos = pedidoService.buscarPedidosPorUsuario(usuario);
            model.addAttribute("pedidos", pedidos);
            return "meus-pedidos";  // Nome do template de pedidos
        } catch (RuntimeException e) {
            return "redirect:/usuario/login"; // Redireciona se o usuário não estiver logado
        }
    }


    // Exibe os detalhes de um pedido específico
    @GetMapping("/pedido/detalhes/{id}")
    public String detalhesPedido(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        model.addAttribute("pedido", pedido);
        return "detalhes-pedido";  // O template que mostra os detalhes do pedido
    }
}

