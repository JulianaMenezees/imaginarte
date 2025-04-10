package com.imaginarte.imaginarte_teste.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Component
@SessionScope
public class Carrinho {
    private Map<Integer, ItemCarrinho> itens = new HashMap<>();
    private double frete = 0.0;

    public void adicionarProduto(ProdutoAdmin produto) {
        ItemCarrinho item = itens.get(produto.getId());
        if (item != null) {
            item.incrementar();
        } else {
            itens.put(produto.getId(), new ItemCarrinho(produto));
        }
    }

    public void removerProduto(int idProduto) {
        itens.remove(idProduto);
    }

    public void atualizarQuantidade(int idProduto, int novaQuantidade) {
        ItemCarrinho item = itens.get(idProduto);
        if (item != null) {
            if (novaQuantidade <= 0) {
                itens.remove(idProduto);
            } else {
                while (item.getQuantidade() < novaQuantidade) item.incrementar();
                while (item.getQuantidade() > novaQuantidade) item.decrementar();
            }
        }
    }

    public Collection<ItemCarrinho> getItens() {
        return itens.values();
    }

    public double getTotal() {
        return getSubtotal() + frete;
    }

    public double getSubtotal() {
        return itens.values().stream().mapToDouble(ItemCarrinho::getSubtotal).sum();
    }

    public int getQuantidadeTotal() {
        return itens.values().stream()
                .mapToInt(ItemCarrinho::getQuantidade)
                .sum();
    }

    public void setFrete(double frete) {
        this.frete = frete;
    }

    public double getFrete() {
        return frete;
    }

    public void limpar() {
        itens.clear();
        frete = 0.0;
    }
}
