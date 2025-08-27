package com.joazao.crudContabilidade.dto;

import java.util.List;

public record VendaCreateDTO(
    Long clienteId,
    String tipoVenda,
    Integer numeroParcelas,
    double valorTotal,
    double valorIcmsDebito,
    List<ItemVendaCreateDTO> itens
) {} 