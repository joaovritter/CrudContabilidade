package com.joazao.crudContabilidade.dto;

public record FornecedorDTO(
    Long id,
    String cnpj,
    String nome,
    String cidade,
    String estado
) {} 