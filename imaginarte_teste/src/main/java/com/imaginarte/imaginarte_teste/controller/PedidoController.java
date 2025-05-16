package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.model.Pedido;
import com.imaginarte.imaginarte_teste.model.StatusPedido;
import com.imaginarte.imaginarte_teste.model.Usuario;
import com.imaginarte.imaginarte_teste.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

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

    @GetMapping("/meus-pedidos/{id}")
    public String visualizarPedido(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        Pedido pedido = pedidoService.buscarPorId(id);

        if (!(pedido.getUsuario().getId() ==(usuario.getId()))) {
            return "redirect:/pedido/meuspedidos";
        }

        model.addAttribute("pedido", pedido);
        return "Usuario/pedidoDetalhes";
    }

    @GetMapping("/todos")
    public String listarTodosOsPedidos(Model model) {
        List<Pedido> pedidos = pedidoService.listarTodosOrdenadosPorData();
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("statusValores", StatusPedido.values());
        return "ProdutosEstoquista/pedidos";
    }

    @PostMapping("/{id}/atualizar-status")
    public String atualizarStatus(@PathVariable Long id, @RequestParam("status") StatusPedido status) {
        pedidoService.atualizarStatus(id, status);
        return "redirect:/pedido/todos";
    }

}
