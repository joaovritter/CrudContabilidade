package com.joazao.crudContabilidade.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "itens_venda")
public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "preco_unitario", nullable = false)
    private double precoUnitario;

    @Column(name = "valor_icms", nullable = false)
    private double valorIcms;

    @Column(name = "valor_total", nullable = false)
    private double valorTotal;

    public void calcularTotais() {
        this.valorTotal = this.quantidade * this.precoUnitario;
        this.valorIcms = this.valorTotal * (this.produto.getIcms() / 100);
    }

    // Construtor padrão para garantir inicialização
    public ItemVenda() {
        this.quantidade = 1;
        this.precoUnitario = 0.0;
        this.valorIcms = 0.0;
        this.valorTotal = 0.0;
    }
} 