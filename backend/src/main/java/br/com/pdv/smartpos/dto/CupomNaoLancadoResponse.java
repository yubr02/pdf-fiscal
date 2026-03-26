package br.com.pdv.smartpos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CupomNaoLancadoResponse(
    Long id,
    String numeroCupom,
    LocalDateTime dataHoraCupom,
    BigDecimal valor,
    String statusCupom
) {
}
