package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FiadoResponse(
    Long id,
    String cliente,
    String cpfCnpj,
    String telefone,
    String numeroVenda,
    BigDecimal valorPendente,
    LocalDate dataVencimento,
    String statusFiado
) {
}
