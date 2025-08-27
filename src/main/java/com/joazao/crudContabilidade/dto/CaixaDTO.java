package com.joazao.crudContabilidade.dto;

import com.joazao.crudContabilidade.model.enums.TipoMovimentacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CaixaDTO(
    Long id,
    LocalDate data,
    TipoMovimentacao tipo,
    BigDecimal valor,
    String descricao,
    Long relacionadoId,
    LocalDateTime criadoEm
) {} 