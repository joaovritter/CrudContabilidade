package com.joazao.crudContabilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "vendas")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime dataVenda;

    @Column(nullable = false)
    private String tipoVenda; // "AVISTA" ou "APRAZO"

    @Column
    private Integer numeroParcelas;

    @Column(nullable = false)
    private double valorTotal;

    @Column(name = "valor_icms_debito", nullable = false)
    private double valorIcmsDebito;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataVenda = LocalDateTime.now();
    }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        item.setVenda(this);
    }

    public void removerItem(ItemVenda item) {
        itens.remove(item);
        item.setVenda(null);
    }
} 