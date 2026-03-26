package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(
    Long id,
    String numeroVenda,
    String operador,
    String cliente,
    String tipoVenda,
    String statusVenda,
    BigDecimal subtotal,
    BigDecimal desconto,
    BigDecimal acrescimo,
    BigDecimal total,
    BigDecimal valorPago,
    BigDecimal troco,
    String formaPagamento,
    Integer quantidadeItens,
    LocalDateTime dataHoraVenda,
    List<ItemVendaResponse> itens
) {
}
