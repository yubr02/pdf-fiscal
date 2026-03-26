package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
    Long id,
    String codigoInterno,
    String codigoBarras,
    String nome,
    String descricao,
    String categoria,
    String subcategoria,
    BigDecimal precoCusto,
    BigDecimal precoVenda,
    BigDecimal estoque,
    String unidade,
    String ncm,
    String cest,
    String cfop,
    String cst,
    String csosn,
    BigDecimal aliquota,
    String origem,
    Boolean ativo
) {
}
