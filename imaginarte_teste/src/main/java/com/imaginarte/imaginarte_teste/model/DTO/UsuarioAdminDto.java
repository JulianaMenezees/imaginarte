package com.imaginarte.imaginarte_teste.model.DTO;

import jakarta.validation.constraints.NotEmpty;

//essa classe obriga o usuário a preencher todos os dados
public class UsuarioAdminDto {

    private int id;

    @NotEmpty(message = "Campo obrigatório")
    private String nome;

    @NotEmpty(message = "Campo obrigatório")
    private String email;

    @NotEmpty(message = "Campo obrigatório")
    private String senha;

    @NotEmpty(message = "Campo obrigatório")
    private String cpf;

    @NotEmpty(message = "Campo obrigatório")
    private String grupo;

    //getters e setters

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

    public @NotEmpty(message = "Campo obrigatório") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Campo obrigatório") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Campo obrigatório") String getSenha() {
        return senha;
    }

    public void setSenha(@NotEmpty(message = "Campo obrigatório") String senha) {
        this.senha = senha;
    }


    public @NotEmpty(message = "Campo obrigatório") String getCpf() {
        return cpf;
    }

    public void setCpf(@NotEmpty(message = "Campo obrigatório") String cpf) {
        this.cpf = cpf;
    }

    public @NotEmpty(message = "Campo obrigatório") String getGrupo() {
        return grupo;
    }

    public void setGrupo(@NotEmpty(message = "Campo obrigatório") String grupo) {
        this.grupo = grupo;
    }
}
