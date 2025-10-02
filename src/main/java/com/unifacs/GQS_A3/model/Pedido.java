package com.unifacs.GQS_A3.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Setter private Long id;
    @Setter private String cliente;
    private List<Produto> produtos = new ArrayList<>();
    private double ValorTotal;

    public void adicionarProdutos(Produto produto) {
        if (produto != null) {
            this.produtos.add(produto);
            this.calcularTotal();
        }
    }

    public void removeProduto(Produto produto) {
        if (produto != null){
            this.produtos.remove(produto);
            calcularTotal();
        }
    }

    private void calcularTotal() {
        ValorTotal = produtos.stream().mapToDouble(Produto::getValorUnit).sum();
    }
}
