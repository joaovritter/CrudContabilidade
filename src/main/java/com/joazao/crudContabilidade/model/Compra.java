package com.joazao.crudContabilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCompra> itensCompra = new ArrayList<>();

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "valor_icms_credito", nullable = false)
    private BigDecimal valorIcmsCredito;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(nullable = false)
    private Integer parcelas; // Total de parcelas

    private Integer parcelasPagas = 0; // Quantidade de parcelas já pagas

    public BigDecimal getSaldoDevedor() {
        if (parcelasPagas == null) return valorTotal;
        return valorTotal.subtract(valorTotal.divide(BigDecimal.valueOf(parcelas)).multiply(BigDecimal.valueOf(parcelasPagas)));
    }
} 