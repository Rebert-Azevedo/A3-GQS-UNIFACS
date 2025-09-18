package com.unifacs.GQS_A3.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Produtos{
    private  String nome;
    private String descricao;
    private double valor_unit;

    public Produtos(String nome, String descricao, double valor_unit){
        this.nome = nome;
        this.descricao = descricao;
        this.valor_unit = valor_unit;
    }
}



