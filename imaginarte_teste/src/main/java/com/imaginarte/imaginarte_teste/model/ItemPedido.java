// ItemPedido.java
package com.imaginarte.imaginarte_teste.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    @ManyToOne
    private ProdutoAdmin produto;

    @ManyToOne
    private Pedido pedido;
}
