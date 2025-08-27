package com.joazao.crudContabilidade.dto;

public record FornecedorCreateDTO(
    String cnpj,
    String nome,
    String cidade,
    String estado
) {} 