package com.joazao.crudContabilidade.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VendaDTO(
    Long id,
    Long clienteId,
    String clienteNome,
    String clienteCpf,
    LocalDateTime dataVenda,
    String tipoVenda,
    Integer numeroParcelas,
    double valorTotal,
    double valorIcmsDebito,
    List<ItemVendaDTO> itens
) {} 