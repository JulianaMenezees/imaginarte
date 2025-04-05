package com.imaginarte.imaginarte_teste.model.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioDto {

    private int id;

    @NotEmpty(message = "Campo obrigatório")
    private String nome;

    @NotEmpty(message = "Campo obrigatório")
    private String email;

    @NotEmpty(message = "Campo obrigatório")
    private String senha;

    @NotEmpty(message = "Campo obrigatório")
    private String cpf;

    @NotNull(message = "Data de nascimento obrigatória")
    @Past(message = "A data deve ser no passado")
    private LocalDate dataNascimento;

    @NotEmpty(message = "Campo obrigatório")
    private String genero;

    @NotEmpty(message = "Campo obrigatório")
    private String cep;

    @NotEmpty(message = "Campo obrigatório")
    private String logradouro;

    @NotEmpty(message = "Campo obrigatório")
    private String numero;

    private String complemento;

    @NotEmpty(message = "Campo obrigatório")
    private String bairro;

    @NotEmpty(message = "Campo obrigatório")
    private String cidade;

    @NotEmpty(message = "Campo obrigatório")
    private String uf;
}
