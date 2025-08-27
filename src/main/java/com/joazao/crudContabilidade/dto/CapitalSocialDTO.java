package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CapitalSocialDTO(
    Long id,
    BigDecimal valorAbertura,
    LocalDate dataAbertura
) {} 