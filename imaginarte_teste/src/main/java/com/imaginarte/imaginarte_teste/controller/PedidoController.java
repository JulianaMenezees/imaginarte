package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import com.imaginarte.imaginarte_teste.service.PedidoService;
import com.imaginarte.imaginarte_teste.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/meuspedidos")
    public String listarPedido(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        List<Pedido> pedidos = pedidoService.buscarPedidosPorUsuario(usuario);
        System.out.println("Pedidos encontrados: " + pedidos.size()); // ADICIONADO PARA DEBUG

        model.addAttribute("pedidos", pedidos);
        return "Usuario/pedidos";
    }

}
