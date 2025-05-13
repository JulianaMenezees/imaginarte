package com.imaginarte.imaginarte_teste.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPedido;
    private BigDecimal total;
    private BigDecimal frete;
    private String formaPagamento;
    private String status;
    private LocalDateTime dataPedido;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private EnderecoEntrega enderecoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @Transient
    public BigDecimal getSubtotal() {
        return itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
