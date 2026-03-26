package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;

public record TributacaoResponse(
    Long id,
    Long produtoId,
    String produto,
    String ncm,
    String cest,
    String cfop,
    String cst,
    String csosn,
    BigDecimal aliquota,
    String origem
) {
}
