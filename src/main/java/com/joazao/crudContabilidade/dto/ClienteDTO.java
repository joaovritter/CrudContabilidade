package com.joazao.crudContabilidade.dto;

public record ClienteDTO(
    Long id,
    String cpf,
    String nome,
    String cidade,
    String estado
) {} 