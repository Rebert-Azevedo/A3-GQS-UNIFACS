package com.unifacs.GQS_A3.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="funcionario")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String nome;
    @Column(nullable = false, unique = true)
    public String email;
    @Column(nullable = false)
    public String senha;
    @Column(nullable = false)
    public LocalDate dataNascimento;
    @Column(nullable = false)
    public double salario;
    @Column(nullable = false)
    public String cargo;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    public Funcionario(){}

    public Funcionario( String nome, String email, String senha, double salario, String cargo, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.salario = salario;
        this.cargo =  cargo;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getSalario(){
        return  this.salario;
    }

    public void setSalario(double salario){
       this.salario = salario;
    }

    public String getCargo(){
        return this.cargo;
    }

    public void setCargo(String cargo){
        this.cargo = cargo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
