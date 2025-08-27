package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompraDTO(
    Long id,
    Long fornecedorId,
    String fornecedorNome,
    List<ItemCompraDTO> itensCompra,
    BigDecimal valorTotal,
    BigDecimal valorIcmsCredito,
    LocalDateTime dataCompra,
    String formaPagamento,
    Integer parcelas,
    Integer parcelasPagas,
    BigDecimal saldoDevedor
) {} 