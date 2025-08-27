package com.joazao.crudContabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PatrimonioCreateDTO(
    String nome,
    String descricao,
    BigDecimal valorAquisicao,
    LocalDate dataAquisicao,
    String tipoBem,
    BigDecimal valorEntrada,
    Integer parcelasTotais,
    Integer parcelasPagas,
    BigDecimal valorParcela,
    LocalDate dataVencimentoPrimeiraParcela,
    Long fornecedorId
) {} 