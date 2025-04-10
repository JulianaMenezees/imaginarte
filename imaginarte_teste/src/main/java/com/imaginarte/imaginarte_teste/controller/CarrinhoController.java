package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.ProdutosRepository;
import com.imaginarte.imaginarte_teste.model.Carrinho;
import com.imaginarte.imaginarte_teste.model.ItemCarrinho;
import com.imaginarte.imaginarte_teste.model.ProdutoAdmin;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private Carrinho carrinho;

    @Autowired
    private ProdutosRepository produtosRepo;

    @GetMapping
    public String verCarrinho(Model model, HttpSession session) {
        model.addAttribute("carrinho", carrinho);
        session.setAttribute("quantidadeItensCarrinho", carrinho.getQuantidadeTotal()); // <-- Adicionado aqui
        return "carrinho";
    }

    @PostMapping("/adicionar/{id}")
    public String adicionarAoCarrinho(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        ProdutoAdmin produto = produtosRepo.findById(id).orElse(null);
        if (produto != null) {
            carrinho.adicionarProduto(produto);
            session.setAttribute("quantidadeItensCarrinho", carrinho.getQuantidadeTotal());
            redirectAttributes.addFlashAttribute("mensagem", "Produto adicionado ao carrinho!");
        }
        return "redirect:/carrinho";
    }


    @PostMapping("/atualizar/{id}")
    public String atualizarQuantidade(@PathVariable int id, @RequestParam int quantidade, HttpSession session) {
        carrinho.atualizarQuantidade(id, quantidade);
        session.setAttribute("quantidadeItensCarrinho", carrinho.getQuantidadeTotal());
        return "redirect:/carrinho";
    }


    @PostMapping("/frete")
    public String escolherFrete(@RequestParam double frete) {
        carrinho.setFrete(frete);
        return "redirect:/carrinho";
    }

    @PostMapping("/remover/{id}")
    public String removerItem(@PathVariable int id, HttpSession session) {
        carrinho.removerProduto(id);
        session.setAttribute("quantidadeItensCarrinho", carrinho.getQuantidadeTotal());
        return "redirect:/carrinho";
    }


    @GetMapping("/comprar/{id}")
    public String comprar(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        ProdutoAdmin produto = produtosRepo.findById(id).orElse(null);
        if (produto != null) {
            carrinho.adicionarProduto(produto);
            session.setAttribute("quantidadeItensCarrinho", carrinho.getQuantidadeTotal()); // <-- Adicionado aqui
            redirectAttributes.addFlashAttribute("mensagem", "Produto adicionado ao carrinho!");
        }
        return "redirect:/carrinho";
    }
}
