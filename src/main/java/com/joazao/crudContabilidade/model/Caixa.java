package com.joazao.crudContabilidade.model;

import com.joazao.crudContabilidade.model.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;
    
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;

    private String descricao;

    private Long relacionadoId; // ID do objeto relacionado (por exemplo, Compra, Venda, etc.)

    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }
}
