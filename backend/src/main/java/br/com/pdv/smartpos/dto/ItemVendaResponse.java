package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;

public record ItemVendaResponse(
    Long id,
    Long produtoId,
    String codigoBarras,
    String descricao,
    BigDecimal quantidade,
    BigDecimal valorUnitario,
    BigDecimal subtotal
) {
}
