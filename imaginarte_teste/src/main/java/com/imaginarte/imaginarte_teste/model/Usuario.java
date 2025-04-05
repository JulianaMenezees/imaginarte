package com.imaginarte.imaginarte_teste.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter @Setter
    @Column(nullable = false, length = 100)
    private String nome;

    @Getter @Setter
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Getter @Setter
    @Column(nullable = false)
    private String senha;

    @Getter @Setter
    @Column(unique = true, length = 11)
    private String cpf;

    @Getter @Setter
    @NotNull(message = "Data de nascimento obrigatória")
    @Past(message = "A data deve ser no passado")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Getter @Setter
    @Column(nullable = false)
    private String genero;

    //endereço

    @Getter @Setter
    @Column(nullable = false, length = 9)
    private String cep;

    @Getter @Setter
    @Column(nullable = false, length = 100)
    private String logradouro;

    @Getter @Setter
    @Column(nullable = false, length = 10)
    private String numero;

    @Getter @Setter
    @Column(length = 50)
    private String complemento;

    @Getter @Setter
    @Column(nullable = false, length = 60)
    private String bairro;

    @Getter @Setter
    @Column(nullable = false, length = 60)
    private String cidade;

    @Getter @Setter
    @Column(nullable = false, length = 2)
    private String uf;



}
