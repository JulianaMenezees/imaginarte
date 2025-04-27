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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/pagamento")
    public String mostrarPagamento() {
        return "checkout/pagamento";  // Thymeleaf vai procurar no templates
    }

    @PostMapping("/checkout/pagamento")
    public String processarPagamento(@RequestParam String formaPagamento,
                                     @RequestParam(required = false) String numeroCartao,
                                     @RequestParam(required = false) String codigoVerificador,
                                     @RequestParam(required = false) String nomeCartao,
                                     @RequestParam(required = false) String dataVencimento,
                                     @RequestParam(required = false) int parcelas,
                                     RedirectAttributes redirectAttributes) {

        if (formaPagamento == null) {
            redirectAttributes.addFlashAttribute("erro", "Por favor, selecione uma forma de pagamento.");
            return "redirect:/checkout/pagamento";
        }

        if (formaPagamento.equals("cartao")) {
            // Lógica para processar o pagamento com cartão
            if (numeroCartao.isEmpty() || codigoVerificador.isEmpty() || nomeCartao.isEmpty() || dataVencimento.isEmpty() || parcelas <= 0) {
                redirectAttributes.addFlashAttribute("erro", "Por favor, preencha todos os campos do cartão de crédito.");
                return "redirect:/checkout/pagamento";
            }
        }
        return "redirect:/checkout/validar";
    }

}
