package com.joazao.crudContabilidade.dto;

public record ProdutoDTO(
    Long id,
    String nome,
    double precoCompra,
    double precoVenda,
    double icms,
    double debito,
    double credito,
    int estoque,
    Long fornecedorId,
    String fornecedorNome
) {} 