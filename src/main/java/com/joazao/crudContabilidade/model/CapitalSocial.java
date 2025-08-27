package com.joazao.crudContabilidade.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CapitalSocial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAbertura;

    @Column(nullable = false)
    private LocalDate dataAbertura;

    public CapitalSocial() {
        this.valorAbertura = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getValorAbertura() { return valorAbertura; }
    public void setValorAbertura(BigDecimal valorAbertura) { this.valorAbertura = valorAbertura; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
} 