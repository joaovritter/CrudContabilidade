package com.joazao.crudContabilidade.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private double precoCompra;
    private double precoVenda;
    private double icms;
    private double debito;
    private double credito;

    public Produto(String nome, double precoCompra, double precoVenda, double icms, double debito, double credito) {
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.icms = icms;
        this.debito = debito;
        this.credito = credito;
    }
}
