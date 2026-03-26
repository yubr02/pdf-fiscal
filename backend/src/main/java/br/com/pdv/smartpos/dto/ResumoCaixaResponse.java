package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoCaixaResponse(
    LocalDate dataCaixa,
    BigDecimal totalVendido,
    BigDecimal totalFiscal,
    BigDecimal totalNaoFiscal,
    Integer quantidadeVendas,
    Boolean fechado
) {
}
