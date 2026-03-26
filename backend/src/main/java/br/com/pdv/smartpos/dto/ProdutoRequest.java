package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequest(
    @NotBlank(message = "Informe o código interno.") String codigoInterno,
    @NotBlank(message = "Informe o código de barras.") String codigoBarras,
    @NotBlank(message = "Informe o nome do produto.") String nome,
    String descricao,
    String categoria,
    String subcategoria,
    @NotNull(message = "Informe o preço de custo.") @DecimalMin(value = "0.00") BigDecimal precoCusto,
    @NotNull(message = "Informe o preço de venda.") @DecimalMin(value = "0.00") BigDecimal precoVenda,
    @NotNull(message = "Informe o estoque.") BigDecimal estoque,
    @NotBlank(message = "Informe a unidade.") String unidade,
    String ncm,
    String cest,
    String cfop,
    String cst,
    String csosn,
    BigDecimal aliquota,
    String origem
) {
}
