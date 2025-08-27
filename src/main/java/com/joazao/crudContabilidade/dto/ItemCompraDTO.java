package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;

public record ItemCompraDTO(
    Long id,
    Long produtoId,
    String produtoNome,
    int quantidade,
    BigDecimal valorUnitario,
    BigDecimal valorTotal
) {} 