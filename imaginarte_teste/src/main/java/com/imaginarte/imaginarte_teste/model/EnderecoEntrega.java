package com.imaginarte.imaginarte_teste.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ENDERECO_ENTREGA")
public class EnderecoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

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

    @Getter @Setter
    private boolean padrao; // Endereço de entrega principal

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @Getter @Setter
    private Usuario usuario;
}

