package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompraCreateDTO(
    Long fornecedorId,
    List<ItemCompraCreateDTO> itensCompra,
    BigDecimal valorTotal,
    BigDecimal valorIcmsCredito,
    LocalDateTime dataCompra,
    String formaPagamento,
    Integer parcelas,
    Integer parcelasPagas
) {} 