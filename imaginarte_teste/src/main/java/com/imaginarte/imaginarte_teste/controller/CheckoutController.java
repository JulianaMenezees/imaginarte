package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.EnderecoEntregaRepository;
import com.imaginarte.imaginarte_teste.Repository.PedidoRepository;
import com.imaginarte.imaginarte_teste.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private EnderecoEntregaRepository enderecoEntregaRepo;

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private Carrinho carrinho;

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

    @PostMapping("/pagamento")
    public String processarPagamento(@RequestParam String formaPagamento,
                                     @RequestParam(required = false) String numeroCartao,
                                     @RequestParam(required = false) String codigoVerificador,
                                     @RequestParam(required = false) String nomeCartao,
                                     @RequestParam(required = false) String dataVencimento,
                                     @RequestParam(required = false) Integer parcelas,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        if (formaPagamento == null) {
            redirectAttributes.addFlashAttribute("erro", "Por favor, selecione uma forma de pagamento.");
            return "redirect:/checkout/pagamento";
        }

        if (formaPagamento.equals("cartao")) {
            if (numeroCartao == null || numeroCartao.isEmpty() ||
                    codigoVerificador == null || codigoVerificador.isEmpty() ||
                    nomeCartao == null || nomeCartao.isEmpty() ||
                    dataVencimento == null || dataVencimento.isEmpty() ||
                    parcelas == null || parcelas <= 0) {
                redirectAttributes.addFlashAttribute("erro", "Por favor, preencha todos os campos do cartão de crédito.");
                return "redirect:/checkout/pagamento";
            }

            // Se tudo ok, salva os dados mínimos na sessão (você pode salvar mais depois se quiser)
            session.setAttribute("formaPagamento", "Cartão de Crédito");
            session.setAttribute("parcelas", parcelas);
        } else if (formaPagamento.equals("boleto")) {
            session.setAttribute("formaPagamento", "Boleto Bancário");
        }

        return "redirect:/checkout/resumo"; // Continua para o resumo
    }

    @GetMapping("/resumo")
    public String mostrarResumo(Model model, HttpSession session) {
        // Recupera o usuário da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/usuario/login"; // Redireciona para login caso não esteja logado
        }

        // Recupera o endereço de entrega selecionado da sessão
        Long enderecoId = (Long) session.getAttribute("enderecoSelecionado");
        EnderecoEntrega enderecoEntrega = enderecoEntregaRepo.findById(enderecoId).orElse(null);

        if (enderecoEntrega == null) {
            return "redirect:/checkout/endereco"; // Redireciona para selecionar um endereço caso não tenha sido escolhido
        }

        // Recupera a forma de pagamento da sessão
        String formaPagamento = (String) session.getAttribute("formaPagamento");

        // Passa os dados para o modelo
        model.addAttribute("carrinho", carrinho);
        model.addAttribute("endereco", enderecoEntrega);
        model.addAttribute("formaPagamento", formaPagamento);

        return "checkout/resumoPedido"; // Nome do template
    }

    @GetMapping("/validar")
    public String validarPedido(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Long enderecoId = (Long) session.getAttribute("enderecoSelecionado");
        String formaPagamento = (String) session.getAttribute("formaPagamento");

        if (usuario == null || enderecoId == null || formaPagamento == null) {
            return "redirect:/usuario/login";
        }

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("endereco", enderecoEntregaRepo.findById(enderecoId).orElse(null));
        model.addAttribute("formaPagamento", formaPagamento);

        return "checkout/resumoPedido"; // checkout/resumoPedido.html
    }

    @PostMapping("/concluir")
    public String concluirPedido(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Long enderecoId = (Long) session.getAttribute("enderecoSelecionado");

        if (usuario == null || enderecoId == null) {
            redirectAttributes.addFlashAttribute("erro", "Sessão expirada, faça login novamente.");
            return "redirect:/usuario/login";
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEnderecoEntrega(enderecoEntregaRepo.findById(enderecoId).orElse(null));
        String formaPagamento = (String) session.getAttribute("formaPagamento");
        pedido.setFormaPagamento(formaPagamento != null ? formaPagamento : "Não Informado");
        pedido.setFrete(BigDecimal.valueOf(carrinho.getFrete()));
        pedido.setTotal(BigDecimal.valueOf(carrinho.getTotal()));
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setDataPedido(LocalDateTime.now());

        pedido.setNumeroPedido(gerarNumeroPedido());

        List<ItemPedido> itensPedido = new ArrayList<>();
        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(itemCarrinho.getProduto());
            itemPedido.setQuantidade(itemCarrinho.getQuantidade());
            itemPedido.setPrecoUnitario(BigDecimal.valueOf(itemCarrinho.getProduto().getPreco()));
            itemPedido.setSubtotal(BigDecimal.valueOf(itemCarrinho.getSubtotal()));
            itensPedido.add(itemPedido);
        }
        pedido.setItens(itensPedido);

        pedidoRepo.save(pedido);

        // Limpa carrinho depois de concluir
        carrinho.limpar();

        redirectAttributes.addFlashAttribute("sucesso", "Pedido #" + pedido.getNumeroPedido() + " realizado com sucesso! Valor total: R$ " + pedido.getTotal());

        return "redirect:/checkout/finalizado";
    }

    @GetMapping("/finalizado")
    public String pedidoFinalizado() {
        return "checkout/pedidoFinalizado"; // criar a tela pedidoFinalizado.html
    }

    private String gerarNumeroPedido() {
        return "PD" + System.currentTimeMillis() + (new Random().nextInt(900) + 100);
    }

}
