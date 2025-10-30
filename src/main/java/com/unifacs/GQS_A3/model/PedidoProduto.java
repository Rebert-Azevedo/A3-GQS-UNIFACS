package com.unifacs.GQS_A3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pedido_produto")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnoreProperties("pedidoProduto")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name= "produto_id")
    @JsonIgnoreProperties("pedidoProduto")
    private Produto produto;

    @Column(name="quantidade", nullable = false)
    private int qtdeProduto;
}
