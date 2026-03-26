package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TributacaoRequest(
    @NotNull(message = "Informe o produto.") Long produtoId,
    String ncm,
    String cest,
    String cfop,
    String cst,
    String csosn,
    BigDecimal aliquota,
    String origem
) {
}
