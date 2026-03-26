package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemVendaRequest(
    @NotNull(message = "Informe o produto.") Long produtoId,
    @NotNull(message = "Informe a quantidade.") @DecimalMin(value = "0.001") BigDecimal quantidade
) {
}
