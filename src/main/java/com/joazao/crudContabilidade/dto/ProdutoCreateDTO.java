package com.joazao.crudContabilidade.dto;

public record ProdutoCreateDTO(
    String nome,
    double precoCompra,
    double precoVenda,
    double icms,
    double debito,
    double credito,
    Long fornecedorId
) {} 