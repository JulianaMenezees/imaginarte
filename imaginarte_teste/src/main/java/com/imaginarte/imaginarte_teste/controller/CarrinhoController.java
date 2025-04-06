package com.imaginarte.imaginarte_teste.controller;

import com.imaginarte.imaginarte_teste.Repository.ProdutosRepository;
import com.imaginarte.imaginarte_teste.model.Carrinho;
import com.imaginarte.imaginarte_teste.model.ItemCarrinho;
import com.imaginarte.imaginarte_teste.model.ProdutoAdmin;
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
    public String verCarrinho(Model model) {
        // Removido: item.getProduto().getImagens().size();
        model.addAttribute("carrinho", carrinho);
        return "carrinho";
    }

    @PostMapping("/adicionar/{id}")
    public String adicionarAoCarrinho(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ProdutoAdmin produto = produtosRepo.findById(id).orElse(null);
        if (produto != null) {
            carrinho.adicionarProduto(produto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto adicionado ao carrinho!");
        }
        return "redirect:/carrinho";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarQuantidade(@PathVariable int id, @RequestParam int quantidade) {
        carrinho.atualizarQuantidade(id, quantidade);
        return "redirect:/carrinho";
    }

    @PostMapping("/frete")
    public String escolherFrete(@RequestParam double frete) {
        carrinho.setFrete(frete);
        return "redirect:/carrinho";
    }

    @PostMapping("/remover/{id}")
    public String removerItem(@PathVariable int id) {
        carrinho.removerProduto(id);
        return "redirect:/carrinho";
    }

    @GetMapping("/comprar/{id}")
    public String comprar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ProdutoAdmin produto = produtosRepo.findById(id).orElse(null);
        if (produto != null) {
            carrinho.adicionarProduto(produto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto adicionado ao carrinho!");
        }
        return "redirect:/carrinho";
    }
}
