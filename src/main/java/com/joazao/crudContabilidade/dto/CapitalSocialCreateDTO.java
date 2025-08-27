package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CapitalSocialCreateDTO(
    BigDecimal valorAbertura,
    LocalDate dataAbertura
) {} 