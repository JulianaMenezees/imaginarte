package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.EnderecoEntregaRepository;
import com.imaginarte.imaginarte_teste.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private EnderecoEntregaRepository enderecoEntregaRepo;

    @GetMapping("/endereco")
    public String selecionarEndereco(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/usuario/login";

        model.addAttribute("enderecos", enderecoEntregaRepo.findByUsuarioId(usuario.getId()));
        return "checkout/selecionarEndereco";
    }

    @PostMapping("/endereco")
    public String salvarEnderecoEscolhido(@RequestParam("enderecoId") Long enderecoId, HttpSession session) {
        session.setAttribute("enderecoSelecionado", enderecoId);
        return "redirect:/checkout/pagamento"; // Depois faz a tela de pagamento
    }
}

