package com.unifacs.GQS_A3.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Produto {
    private  String nome;
    private String descricao;
    private double preco;

    public Produto(String nome, String descricao, double preco){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }
}



