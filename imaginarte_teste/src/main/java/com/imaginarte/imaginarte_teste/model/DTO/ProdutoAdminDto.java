package com.imaginarte.imaginarte_teste.model.DTO;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProdutoAdminDto {

    private int id;

    @NotEmpty(message = "Campo obrigatório")
    private String nome;

    @NotEmpty(message = "Campo obrigatório")
    private String avaliacao;

    @NotEmpty(message = "Campo obrigatório")
    private String descricao;

    @NotNull(message = "Campo obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private double preco;

    @NotNull
    @Min(1)
    private int quantidade;

    @Transient
    private List<MultipartFile> imagens;

    public List<MultipartFile> getImagens() {
        return imagens;
    }

    public void setImagens(List<MultipartFile> imagens) {
        this.imagens = imagens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "Campo obrigatório") String getNome() {
        return nome;
    }

    public void setNome(@NotEmpty(message = "Campo obrigatório") String nome) {
        this.nome = nome;
    }

    public @NotEmpty(message = "Campo obrigatório") String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(@NotEmpty(message = "Campo obrigatório") String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public @NotEmpty(message = "Campo obrigatório") String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NotEmpty(message = "Campo obrigatório") String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(@NotEmpty(message = "Campo obrigatório") double preco) {
        this.preco = preco;
    }

    @NotNull
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(@NotNull int quantidade) {
        this.quantidade = quantidade;
    }
}
