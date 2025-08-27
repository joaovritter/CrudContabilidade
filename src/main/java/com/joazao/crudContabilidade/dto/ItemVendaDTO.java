package com.joazao.crudContabilidade.dto;

public record ItemVendaDTO(
    Long id,
    Long produtoId,
    String produtoNome,
    int quantidade,
    double precoUnitario,
    double valorIcms,
    double valorTotal
) {} 