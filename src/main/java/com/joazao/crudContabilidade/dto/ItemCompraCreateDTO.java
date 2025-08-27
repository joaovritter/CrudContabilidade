package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;

public record ItemCompraCreateDTO(
    Long produtoId,
    int quantidade,
    BigDecimal valorUnitario,
    BigDecimal valorTotal
) {} 