package com.imaginarte.imaginarte_teste.model;

public class ItemCarrinho {
    private ProdutoAdmin produto;
    private int quantidade;

    public ItemCarrinho(ProdutoAdmin produto) {
        this.produto = produto;
        this.quantidade = 1;
    }

    public ProdutoAdmin getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }

    public void incrementar() { this.quantidade++; }
    public void decrementar() { this.quantidade--; }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
}
