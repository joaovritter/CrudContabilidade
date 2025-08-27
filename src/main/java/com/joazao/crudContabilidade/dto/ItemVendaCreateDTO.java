package com.joazao.crudContabilidade.dto;

public record ItemVendaCreateDTO(
    Long produtoId,
    int quantidade,
    double precoUnitario
) {} 