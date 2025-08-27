package com.joazao.crudContabilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.model.TipoBem;

@Entity
@Data
@Table(name = "patrimonios")
public class Patrimonio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAquisicao;

    @Column(nullable = false)
    private LocalDate dataAquisicao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoBem tipoBem;

    // Campos para controle de contas a pagar de bens (se for a prazo)
    @Column(precision = 10, scale = 2)
    private BigDecimal valorEntrada;
    private Integer parcelasTotais;
    private Integer parcelasPagas;
    @Column(precision = 10, scale = 2)
    private BigDecimal valorParcela;
    private LocalDate dataVencimentoPrimeiraParcela;

    // Relacionamento com Fornecedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @PrePersist
    protected void onCreate() {
        if (dataAquisicao == null) {
            dataAquisicao = LocalDate.now();
        }
    }
} 