package com.joazao.crudContabilidade.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "preco_compra", nullable = false)
    private double precoCompra;

    @Column(name = "preco_venda", nullable = false)
    private double precoVenda;

    @Column(nullable = false)
    private double icms;

    @Column(nullable = false)
    private double debito;

    @Column(nullable = false)
    private double credito;

    @Column(nullable = false)
    private int estoque = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    public Produto() {
    }

    public Produto(String nome, double precoCompra, double precoVenda, double icms, double debito, double credito) {
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.icms = icms;
        this.debito = debito;
        this.credito = credito;
    }

    public Produto(Long id, String nome, double precoCompra, double precoVenda, double icms, double debito, double credito) {
        this.id = id;
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.icms = icms;
        this.debito = debito;
        this.credito = credito;
    }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    // Calcula o preço de custo
    public double calcularPrecoCusto() {
        return precoCompra + (precoCompra * 0.17); // Adiciona ICMS de 17%
    }

    // Calcula o lucro
    public double calcularLucro() {
        return precoVenda - calcularPrecoCusto();
    }

    // Calcula o valor do ICMS
    public double calcularValorIcms() {
        return precoVenda * (icms / 100);
    }

    // Calcula o valor total com ICMS
    public double calcularValorTotalComIcms() {
        return precoVenda + calcularValorIcms();
    }

    // Vender produto à vista
    public String venderAVista() {
        double valorIcms = calcularValorIcms();
        double valorTotal = calcularValorTotalComIcms();
        return String.format("Produto vendido à vista por R$ %.2f\n" +
                           "Valor do ICMS: R$ %.2f\n" +
                           "Valor total com ICMS: R$ %.2f",
                           precoVenda, valorIcms, valorTotal);
    }

    // Vender produto a prazo
    public String venderAPrazo(int parcelas) {
        double valorIcms = calcularValorIcms();
        double valorTotal = calcularValorTotalComIcms();
        double valorParcela = valorTotal / parcelas;
        
        return String.format("Produto vendido a prazo em %d parcelas\n" +
                           "Valor de cada parcela: R$ %.2f\n" +
                           "Valor do ICMS: R$ %.2f\n" +
                           "Valor total com ICMS: R$ %.2f",
                           parcelas, valorParcela, valorIcms, valorTotal);
    }
}