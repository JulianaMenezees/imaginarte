package com.imaginarte.imaginarte_teste.model;

import jakarta.persistence.*;

@Entity
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoAdmin produto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProdutoAdmin getProduto() {
        return produto;
    }

    public void setProduto(ProdutoAdmin produto) {
        this.produto = produto;
    }
}
